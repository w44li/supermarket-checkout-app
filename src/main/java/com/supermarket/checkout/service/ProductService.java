package com.supermarket.checkout.service;

import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Product business logic
 * 
 * @Service: Marks this as a Spring service component
 * Services contain business logic and sit between Controllers and Repositories
 * 
 * Benefits of the service layer:
 * - Keeps controllers thin
 * - Reusable business logic
 * - Transaction management
 * - Easy to test
 */
@Service
public class ProductService {

    /**
     * Dependency Injection with @Autowired
     * Spring automatically provides an instance of ProductRepository
     * 
     * Alternative: Constructor injection (recommended for required dependencies)
     */
    private final ProductRepository productRepository;

    /**
     * Constructor injection (preferred over @Autowired fields)
     * Spring automatically injects the ProductRepository
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Find product by ID
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Save a new product (business logic: validate before saving)
     */
    public Product saveProduct(Product product) {
        // Business validation
        validateProduct(product);
        
        return productRepository.save(product);
    }

    /**
     * Update an existing product
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isEmpty()) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }

        Product product = existingProduct.get();
        
        // Update fields
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        
        // Validate before saving
        validateProduct(product);
        
        return productRepository.save(product);
    }

    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    /**
     * Search products by name
     */
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Find products in price range
     */
    public List<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Prices cannot be negative");
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Private method for product validation (business logic)
     */
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be null or negative");
        }
    }
}