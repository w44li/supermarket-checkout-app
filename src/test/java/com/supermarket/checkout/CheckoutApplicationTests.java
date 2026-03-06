package com.supermarket.checkout;

import com.supermarket.checkout.controller.CheckoutController;
import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Spring Boot application
 * 
 * @SpringBootTest: Loads the complete Spring application context for testing
 * This is an integration test - it tests the full application stack
 */
@SpringBootTest
class CheckoutApplicationTests {

    /**
     * Spring automatically injects these components for testing
     */
    @Autowired
    private CheckoutController checkoutController;

    @Autowired
    private ProductService productService;

    /**
     * Test that the Spring context loads successfully
     * This is a "smoke test" - if Spring can't start, this test fails
     */
    @Test
    void contextLoads() {
        // If we get here, Spring Boot started successfully
        assertNotNull(checkoutController);
        assertNotNull(productService);
        System.out.println("✅ Spring Boot context loaded successfully!");
    }

    /**
     * Test that sample data was loaded
     */
    @Test
    void testSampleDataLoaded() {
        List<Product> products = productService.getAllProducts();
        
        assertFalse(products.isEmpty(), "Sample products should be loaded");
        assertTrue(products.size() >= 10, "Should have at least 10 sample products");
        
        System.out.println("✅ Found " + products.size() + " products in database");
    }

    /**
     * Test product search functionality
     */
    @Test
    void testProductSearch() {
        List<Product> appleProducts = productService.searchProductsByName("apple");
        
        assertFalse(appleProducts.isEmpty(), "Should find apple products");
        
        // Check that all results contain "apple" (case-insensitive)
        for (Product product : appleProducts) {
            assertTrue(product.getName().toLowerCase().contains("apple"),
                    "Product name should contain 'apple': " + product.getName());
        }
        
        System.out.println("✅ Found " + appleProducts.size() + " apple products");
    }

    /**
     * Test product creation and retrieval
     */
    @Test
    void testProductCreationAndRetrieval() {
        // Create a test product
        Product testProduct = new Product(
            "Test Product",
            new BigDecimal("9.99")
        );

        // Save the product
        Product savedProduct = productService.saveProduct(testProduct);
        
        assertNotNull(savedProduct.getId(), "Saved product should have an ID");
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(new BigDecimal("9.99"), savedProduct.getPrice());

        // Retrieve the product by ID
        Product retrievedProduct = productService.getProductById(savedProduct.getId()).orElse(null);
        
        assertNotNull(retrievedProduct, "Should be able to retrieve saved product");
        assertEquals(savedProduct.getName(), retrievedProduct.getName());

        System.out.println("✅ Successfully created and retrieved product: " + savedProduct.getName());
    }
}