package com.catering.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCatererId(Long catererId);
    Optional<MenuItem> findByNameIgnoreCaseAndCatererId(String name, Long catererId);
}
