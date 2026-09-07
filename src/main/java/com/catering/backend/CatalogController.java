package com.catering.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.backend.dto.CatererDTO;
import com.catering.backend.dto.LocationDTO;
import com.catering.backend.dto.MenuItemDTO;
import com.catering.backend.service.CatalogService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(catalogService.getAllLocations());
    }

    @GetMapping("/locations/{location}/caterers")
    public ResponseEntity<List<CatererDTO>> getCaterersByLocation(@PathVariable String location) {
        return ResponseEntity.ok(catalogService.getCaterersByLocation(location));
    }

    @GetMapping("/caterers")
    public ResponseEntity<List<CatererDTO>> getAllCaterers() {
        return ResponseEntity.ok(catalogService.getAllCaterers());
    }

    @GetMapping("/caterers/{catererId}")
    public ResponseEntity<CatererDTO> getCatererById(@PathVariable Long catererId) {
        return ResponseEntity.ok(catalogService.getCatererById(catererId));
    }

    @GetMapping("/caterers/{catererId}/menu")
    public ResponseEntity<List<MenuItemDTO>> getMenuByCatererId(@PathVariable Long catererId) {
        return ResponseEntity.ok(catalogService.getMenuByCatererId(catererId));
    }
}
