package com.catering.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catering.backend.Caterer;
import com.catering.backend.CatererRepository;
import com.catering.backend.Location;
import com.catering.backend.LocationRepository;
import com.catering.backend.MenuItem;
import com.catering.backend.MenuItemRepository;
import com.catering.backend.Order;
import com.catering.backend.OrderItem;
import com.catering.backend.OrderRepository;
import com.catering.backend.OrderStatus;
import com.catering.backend.User;
import com.catering.backend.UserRepository;
import com.catering.backend.dto.OrderItemRequestDTO;
import com.catering.backend.dto.OrderItemResponseDTO;
import com.catering.backend.dto.OrderRequestDTO;
import com.catering.backend.dto.OrderResponseDTO;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CatererRepository catererRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Order order = new Order();

        // 1. Resolve or create User
        User user = resolveUser(request);
        order.setUser(user);

        // 2. Resolve Caterer
        Caterer caterer = resolveCaterer(request);
        order.setCaterer(caterer);

        // 3. Resolve Location
        String locationStr = request.getLocation();
        if (locationStr == null && caterer != null && caterer.getLocation() != null) {
            locationStr = caterer.getLocation().getName();
        }
        order.setServiceLocation(locationStr);

        // 4. Contact & metadata
        String customerName = request.getCustomerName();
        if ((customerName == null || customerName.trim().isEmpty()) && user != null) {
            customerName = user.getName();
        }
        order.setCustomerName(customerName != null ? customerName : "Guest Customer");
        order.setCustomerEmail(request.getCustomerEmail() != null ? request.getCustomerEmail() : (user != null ? user.getEmail() : null));
        order.setCustomerPhone(request.getCustomerPhone() != null ? request.getCustomerPhone() : (user != null ? user.getPhone() : null));
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setCustomerNotes(request.getCustomerNotes());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // Parse event date if provided
        if (request.getEventDate() != null && !request.getEventDate().trim().isEmpty()) {
            try {
                order.setEventDate(LocalDate.parse(request.getEventDate().trim()));
            } catch (DateTimeParseException ignored) {
            }
        }

        // 5. Process Line Items
        double calculatedTotal = 0.0;

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (OrderItemRequestDTO itemDto : request.getItems()) {
                MenuItem menuItem = null;
                if (itemDto.getMenuItemId() != null) {
                    menuItem = menuItemRepository.findById(itemDto.getMenuItemId()).orElse(null);
                } else if (itemDto.getName() != null && caterer != null) {
                    menuItem = menuItemRepository.findByNameIgnoreCaseAndCatererId(itemDto.getName().trim(), caterer.getId()).orElse(null);
                }

                String itemName = itemDto.getName();
                if (itemName == null && menuItem != null) {
                    itemName = menuItem.getName();
                }
                if (itemName == null) {
                    itemName = "Catering Item";
                }

                Double price = itemDto.getPrice();
                if (price == null && menuItem != null) {
                    price = menuItem.getPrice();
                }
                if (price == null) {
                    price = 0.0;
                }

                int quantity = itemDto.getQuantity() != null && itemDto.getQuantity() > 0 ? itemDto.getQuantity() : 1;

                OrderItem orderItem = new OrderItem(order, menuItem, itemName, price, quantity);
                order.addOrderItem(orderItem);
                calculatedTotal += orderItem.getSubtotal();
            }

            // Sync first item to legacy fields for backward compatibility
            OrderItem first = order.getOrderItems().get(0);
            order.setFoodItem(first.getItemName());
            order.setQuantity(first.getQuantity());
        } else if (request.getFoodItem() != null && !request.getFoodItem().trim().isEmpty()) {
            // Legacy single-item fallback
            int quantity = request.getQuantity() != null && request.getQuantity() > 0 ? request.getQuantity() : 1;
            double price = request.getTotalAmount() != null && request.getTotalAmount() > 0 ? request.getTotalAmount() : 0.0;
            OrderItem orderItem = new OrderItem(order, null, request.getFoodItem().trim(), price / quantity, quantity);
            order.addOrderItem(orderItem);
            calculatedTotal += price;

            order.setFoodItem(request.getFoodItem().trim());
            order.setQuantity(quantity);
        }

        // Set total amount (favor explicit positive total, or calculated sum)
        if (request.getTotalAmount() != null && request.getTotalAmount() > 0) {
            order.setTotalAmount(request.getTotalAmount());
        } else {
            order.setTotalAmount(calculatedTotal);
        }

        // Persist order and cascade items
        Order saved = orderRepository.save(order);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToResponseDTO(order);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return mapToResponseDTO(updated);
    }

    private User resolveUser(OrderRequestDTO request) {
        if (request.getUserId() != null) {
            Optional<User> byId = userRepository.findById(request.getUserId());
            if (byId.isPresent()) {
                return byId.get();
            }
        }

        String email = request.getCustomerEmail();
        if (email != null && !email.trim().isEmpty()) {
            Optional<User> byEmail = userRepository.findByEmail(email.trim().toLowerCase());
            if (byEmail.isPresent()) {
                return byEmail.get();
            }
            // Create user from customer info
            User newUser = new User(
                    request.getCustomerName() != null ? request.getCustomerName() : "Customer",
                    email.trim().toLowerCase(),
                    request.getCustomerPhone(),
                    request.getDeliveryAddress(),
                    "CUSTOMER"
            );
            return userRepository.save(newUser);
        }

        // If customer phone is provided, try lookup
        if (request.getCustomerPhone() != null && !request.getCustomerPhone().trim().isEmpty()) {
            Optional<User> byPhone = userRepository.findByPhone(request.getCustomerPhone().trim());
            if (byPhone.isPresent()) {
                return byPhone.get();
            }
        }

        // Fallback to existing guest user or first user
        return userRepository.findAll().stream().findFirst().orElseGet(() -> {
            User guest = new User("Guest Customer", "guest@catering.com", "9876543210", "Hyderabad", "CUSTOMER");
            return userRepository.save(guest);
        });
    }

    private Caterer resolveCaterer(OrderRequestDTO request) {
        if (request.getCatererId() != null) {
            Optional<Caterer> byId = catererRepository.findById(request.getCatererId());
            if (byId.isPresent()) {
                return byId.get();
            }
        }

        if (request.getCaterer() != null && !request.getCaterer().trim().isEmpty()) {
            Optional<Caterer> byName = catererRepository.findByNameIgnoreCase(request.getCaterer().trim());
            if (byName.isPresent()) {
                return byName.get();
            }
        }

        return null;
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setServiceLocation(order.getServiceLocation());
        dto.setCatererId(order.getCaterer() != null ? order.getCaterer().getId() : null);
        dto.setCatererName(order.getCaterer() != null ? order.getCaterer().getName() : null);
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : OrderStatus.PENDING.name());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setEventDate(order.getEventDate());
        dto.setCustomerNotes(order.getCustomerNotes());
        dto.setCreatedAt(order.getCreatedAt());

        if (order.getOrderItems() != null) {
            List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream()
                    .map(item -> new OrderItemResponseDTO(
                            item.getId(),
                            item.getMenuItem() != null ? item.getMenuItem().getId() : null,
                            item.getItemName(),
                            item.getPrice(),
                            item.getQuantity(),
                            item.getSubtotal()
                    ))
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }
}
