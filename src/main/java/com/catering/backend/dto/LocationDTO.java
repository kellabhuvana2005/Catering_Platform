package com.catering.backend.dto;

public class LocationDTO {
    private Long id;
    private String name;
    private String code;
    private String city;
    private int catererCount;

    public LocationDTO() {
    }

    public LocationDTO(Long id, String name, String code, String city, int catererCount) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.city = city;
        this.catererCount = catererCount;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCatererCount() {
        return catererCount;
    }

    public void setCatererCount(int catererCount) {
        this.catererCount = catererCount;
    }
}
