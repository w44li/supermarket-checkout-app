package com.supermarket.checkout.config;

import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Data initialization component
 * 
 * @Component: Marks this as a Spring-managed component
 * @PostConstruct: Method runs after Spring creates this component
 * 
 * This class demonstrates:
 * - Component lifecycle (@PostConstruct)
 * - Dependency injection
 * - Working with repositories
 */
@Component
public class DataInitializer {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * This method runs automatically after the application starts
     * @PostConstruct ensures this runs after dependency injection is complete
     */
    @PostConstruct
    public void initializeData() {
        // Only add sample data if no products exist
        if (productRepository.count() == 0) {
            System.out.println("🔧 Initializing sample product data...");
            
            createSampleProducts();
            
            System.out.println("✅ Sample data initialized successfully!");
            System.out.println("📊 Total products in database: " + productRepository.count());
        } else {
            System.out.println("📊 Database already contains " + productRepository.count() + " products");
        }
    }

    private void createSampleProducts() {
        // Create some sample products
        Product[] products = {
            new Product("Red Apple", new BigDecimal("0.99")),
            new Product("Banana Bunch", new BigDecimal("1.29")),
            new Product("Whole Milk", new BigDecimal("2.49")),
            new Product("Sliced Bread", new BigDecimal("2.99")),
            new Product("Orange Juice", new BigDecimal("3.49")),
            new Product("Chicken Breast", new BigDecimal("7.99")),
            new Product("Cheddar Cheese", new BigDecimal("4.29")),
            new Product("Pasta", new BigDecimal("1.99")),
            new Product("Tomato Sauce", new BigDecimal("1.79")),
            new Product("Ice Cream", new BigDecimal("5.99"))
        };

        // Save all products to database
        for (Product product : products) {
            productRepository.save(product);
            System.out.println("➕ Added: " + product.getName() + " ($" + product.getPrice() + ")");
        }
    }
}