package com.catering.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.catering.backend.dto.OrderItemRequestDTO;
import com.catering.backend.dto.OrderRequestDTO;
import com.catering.backend.dto.OrderResponseDTO;
import com.catering.backend.service.CatalogService;
import com.catering.backend.service.OrderService;

@SpringBootTest
class CateringBackendApplicationTests {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CatererRepository catererRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void contextLoads() {
        assertNotNull(catalogService);
        assertNotNull(orderService);
    }

    @Test
    void testSeededLocationsAndCaterers() {
        assertTrue(locationRepository.count() >= 8, "Expected at least 8 locations seeded");
        assertTrue(catererRepository.count() >= 9, "Expected at least 9 caterers seeded");
        assertTrue(menuItemRepository.count() >= 16, "Expected at least 16 menu items seeded");

        var locations = catalogService.getAllLocations();
        assertEquals(8, locations.size());

        var kukatpallyCaterers = catalogService.getCaterersByLocation("kukatpally");
        assertEquals(2, kukatpallyCaterers.size());
    }

    @Test
    void testCreateOrderWithRelationalEntities() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerName("Jane Doe");
        request.setCustomerEmail("jane@example.com");
        request.setCustomerPhone("9876543219");
        request.setLocation("kukatpally");
        request.setCaterer("Sri Sai Catering");
        request.setDeliveryAddress("Plot 12, Phase 1, Kukatpally");
        request.setCustomerNotes("Please deliver by 12 PM");

        OrderItemRequestDTO item1 = new OrderItemRequestDTO(null, "Veg Biryani", 120.0, 2);
        OrderItemRequestDTO item2 = new OrderItemRequestDTO(null, "Paneer Butter Masala", 150.0, 1);
        request.setItems(Arrays.asList(item1, item2));
        request.setTotalAmount(390.0);

        OrderResponseDTO response = orderService.createOrder(request);

        assertNotNull(response.getId());
        assertEquals("Jane Doe", response.getCustomerName());
        assertEquals("Sri Sai Catering", response.getCatererName());
        assertEquals(390.0, response.getTotalAmount());
        assertEquals(2, response.getItems().size());
        assertEquals("PENDING", response.getStatus());

        // Verify retrieval
        OrderResponseDTO retrieved = orderService.getOrderById(response.getId());
        assertNotNull(retrieved);
        assertEquals(response.getId(), retrieved.getId());
        assertEquals(2, retrieved.getItems().size());
    }
}
