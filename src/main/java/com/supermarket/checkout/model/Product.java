package com.supermarket.checkout.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Product entity class representing items in our supermarket
 * 
 * @Entity: Tells Spring/JPA that this class represents a database table
 * @Table: Specifies the table name (optional - defaults to class name)
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * @Id: Marks this field as the primary key
     * @GeneratedValue: Auto-generates the ID value
     * IDENTITY strategy: Uses database auto-increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column: Specifies column properties
     * nullable = false: This field is required
     */
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Default constructor (required by JPA)
     */
    public Product() {
    }

    /**
     * Constructor for creating products
     */
    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    // Getter and Setter methods (required for JPA and Spring)
    
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * toString method for easy debugging
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}