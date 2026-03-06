package com.supermarket.checkout.repository;

import com.supermarket.checkout.model.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory repository for Product operations
 * 
 * @Repository: Marks this as a Spring repository component
 * 
 * This implementation uses a simple Map to store products in memory
 * No database required - all data is stored in application memory
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    /**
     * Find all products
     */
    List<Product> findAll();

    /**
     * Find product by ID
     */
    Optional<Product> findById(Long id);

    /**
     * Save a new product (returns the saved product with ID)
     */
    Product save(Product product);

    /**
     * Count total products in memory
     */
    long count();
}
