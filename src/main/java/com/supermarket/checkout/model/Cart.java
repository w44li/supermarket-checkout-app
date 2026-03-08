package com.supermarket.checkout.model;

import java.util.List;

import jakarta.validation.Valid;

// Cart class to hold  list of items being purchased
public class Cart {

    @Valid
    private List<CartItem> items;

    public Cart() {}

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

}