package com.supermarket.checkout.repository;

import com.supermarket.checkout.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Product database operations
 * 
 * @Repository: Marks this as a Spring repository component
 * 
 * JpaRepository<Product, Long>: 
 * - Product: The entity type this repository manages
 * - Long: The type of the entity's primary key
 * 
 * Spring automatically implements this interface! You don't need to write the code.
 * It provides methods like: save(), findById(), findAll(), delete(), etc.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Custom query methods - Spring generates the SQL automatically!
     * 
     * Method naming conventions:
     * - findBy[FieldName]: Find entities by a field value
     * - findBy[FieldName]And[AnotherField]: Multiple conditions with AND
     * - findBy[FieldName]Or[AnotherField]: Multiple conditions with OR
     */

    /**
     * Find all products containing a name (case-insensitive)
     * Spring generates: SELECT * FROM products WHERE LOWER(name) LIKE LOWER('%searchTerm%')
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find products within a price range
     * Spring generates: SELECT * FROM products WHERE price BETWEEN ? AND ?
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find products cheaper than a given price
     * Spring generates: SELECT * FROM products WHERE price < ?
     */
    List<Product> findByPriceLessThan(BigDecimal price);

    /**
     * Find products more expensive than a given price
     * Spring generates: SELECT * FROM products WHERE price > ?
     */
    List<Product> findByPriceGreaterThan(BigDecimal price);

    /**
     * Custom JPQL query (when method names get too complex)
     * @Query: Allows you to write custom SQL-like queries
     */
    @Query("SELECT p FROM Product p WHERE p.price > ?1 ORDER BY p.price ASC")
    List<Product> findExpensiveProducts(BigDecimal minPrice);

    /**
     * Count products by name pattern
     */
    long countByNameContainingIgnoreCase(String name);
}