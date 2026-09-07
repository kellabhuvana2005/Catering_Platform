package com.catering.backend.dto;

public class OrderItemRequestDTO {
    private Long menuItemId;
    private String name;
    private Double price;
    private Integer quantity = 1;

    public OrderItemRequestDTO() {
    }

    public OrderItemRequestDTO(Long menuItemId, String name, Double price, Integer quantity) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity != null && quantity > 0 ? quantity : 1;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity != null && quantity > 0 ? quantity : 1;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity != null && quantity > 0 ? quantity : 1;
    }
}
