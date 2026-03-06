package com.supermarket.checkout.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.NotNull;

@Table("OFFERS")
public class Offer {

    @Id
    private Long id;

    @NotNull
    private Long productId;

    private Integer requiredQuantity;

    private Double bundlePrice;

    public Offer() {
    }

    public Offer(Long id, Long productId, Integer requiredQuantity, Double bundlePrice) {
        this.id = id;
        this.productId = productId;
        this.requiredQuantity = requiredQuantity;
        this.bundlePrice = bundlePrice;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public Double getBundlePrice() {
        return bundlePrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public void setBundlePrice(Double bundlePrice) {
        this.bundlePrice = bundlePrice;
    }
}