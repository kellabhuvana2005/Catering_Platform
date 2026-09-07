package com.catering.backend.service;

import java.util.List;

import com.catering.backend.dto.CatererDTO;
import com.catering.backend.dto.LocationDTO;
import com.catering.backend.dto.MenuItemDTO;

public interface CatalogService {
    List<LocationDTO> getAllLocations();
    List<CatererDTO> getCaterersByLocation(String locationIdentifier);
    List<CatererDTO> getAllCaterers();
    CatererDTO getCatererById(Long catererId);
    List<MenuItemDTO> getMenuByCatererId(Long catererId);
}
