package com.catering.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByCatererId(Long catererId);
    List<Order> findAllByOrderByCreatedAtDesc();
}
