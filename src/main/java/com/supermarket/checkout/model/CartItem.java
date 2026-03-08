package com.supermarket.checkout.model;

public class CartItem {

    private Long productId;

    // Name of the product being purchased
    private String name;

    // units of the product being purchased
    private Integer quantity;

    public CartItem() {
    }

    public CartItem(Long productId, String name, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
