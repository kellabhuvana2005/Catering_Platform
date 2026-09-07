package com.catering.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatererRepository extends JpaRepository<Caterer, Long> {
    List<Caterer> findByLocationId(Long locationId);
    List<Caterer> findByLocationCodeIgnoreCase(String code);
    Optional<Caterer> findByNameIgnoreCase(String name);
}
