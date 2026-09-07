package com.catering.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class CatererDTO {
    private Long id;
    private String name;
    private String description;
    private String contactPhone;
    private Long locationId;
    private String locationName;
    private String locationCode;
    private List<MenuItemDTO> menu = new ArrayList<>();

    public CatererDTO() {
    }

    public CatererDTO(Long id, String name, String description, String contactPhone, Long locationId, String locationName, String locationCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contactPhone = contactPhone;
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationCode = locationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public List<MenuItemDTO> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItemDTO> menu) {
        this.menu = menu;
    }
}
