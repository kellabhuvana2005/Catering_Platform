package com.catering.backend.service;

import java.util.ArrayList;
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
import com.catering.backend.dto.CatererDTO;
import com.catering.backend.dto.LocationDTO;
import com.catering.backend.dto.MenuItemDTO;

@Service
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CatererRepository catererRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(loc -> new LocationDTO(
                        loc.getId(),
                        loc.getName(),
                        loc.getCode(),
                        loc.getCity(),
                        loc.getCaterers() != null ? loc.getCaterers().size() : 0
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CatererDTO> getCaterersByLocation(String locationIdentifier) {
        if (locationIdentifier == null || locationIdentifier.trim().isEmpty()) {
            return getAllCaterers();
        }

        // Try lookup by numeric ID first
        try {
            Long locId = Long.parseLong(locationIdentifier.trim());
            return catererRepository.findByLocationId(locId).stream()
                    .map(this::mapCatererToDTO)
                    .collect(Collectors.toList());
        } catch (NumberFormatException ignored) {
        }

        // Try lookup by location code (e.g., "kukatpally")
        List<Caterer> byCode = catererRepository.findByLocationCodeIgnoreCase(locationIdentifier.trim());
        if (!byCode.isEmpty()) {
            return byCode.stream().map(this::mapCatererToDTO).collect(Collectors.toList());
        }

        // Try lookup by location name (e.g., "Kukatpally")
        Optional<Location> locByName = locationRepository.findByNameIgnoreCase(locationIdentifier.trim());
        if (locByName.isPresent()) {
            return catererRepository.findByLocationId(locByName.get().getId()).stream()
                    .map(this::mapCatererToDTO)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public List<CatererDTO> getAllCaterers() {
        return catererRepository.findAll().stream()
                .map(this::mapCatererToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CatererDTO getCatererById(Long catererId) {
        Caterer caterer = catererRepository.findById(catererId)
                .orElseThrow(() -> new RuntimeException("Caterer not found with id: " + catererId));
        return mapCatererToDTO(caterer);
    }

    @Override
    public List<MenuItemDTO> getMenuByCatererId(Long catererId) {
        return menuItemRepository.findByCatererId(catererId).stream()
                .map(this::mapMenuItemToDTO)
                .collect(Collectors.toList());
    }

    private CatererDTO mapCatererToDTO(Caterer caterer) {
        CatererDTO dto = new CatererDTO(
                caterer.getId(),
                caterer.getName(),
                caterer.getDescription(),
                caterer.getContactPhone(),
                caterer.getLocation() != null ? caterer.getLocation().getId() : null,
                caterer.getLocation() != null ? caterer.getLocation().getName() : null,
                caterer.getLocation() != null ? caterer.getLocation().getCode() : null
        );

        if (caterer.getMenuItems() != null) {
            dto.setMenu(caterer.getMenuItems().stream()
                    .map(this::mapMenuItemToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private MenuItemDTO mapMenuItemToDTO(MenuItem item) {
        return new MenuItemDTO(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getCategory(),
                item.getDescription(),
                item.isAvailable()
        );
    }
}
