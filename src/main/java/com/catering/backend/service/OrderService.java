package com.catering.backend.service;

import java.util.List;

import com.catering.backend.OrderStatus;
import com.catering.backend.dto.OrderRequestDTO;
import com.catering.backend.dto.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderById(Long id);
    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status);
}
