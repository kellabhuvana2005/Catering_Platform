package com.catering.backend;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCodeIgnoreCase(String code);
    Optional<Location> findByNameIgnoreCase(String name);
}
