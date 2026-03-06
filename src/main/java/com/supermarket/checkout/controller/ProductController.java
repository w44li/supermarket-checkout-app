package com.supermarket.checkout.controller;

import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Product operations
 * 
 * This demonstrates a complete CRUD API:
 * - Create: POST /api/products
 * - Read: GET /api/products, GET /api/products/{id}
 * - Update: PUT /api/products/{id}
 * - Delete: DELETE /api/products/{id}
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor injection of ProductService
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products
     * GET /api/products
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Get product by ID
     * GET /api/products/1
     * 
     * ResponseEntity: Allows us to control HTTP status codes and headers
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    /**
     * Create a new product
     * POST /api/products
     * 
     * @RequestBody: Converts JSON in request body to Product object
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct); // HTTP 201
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // HTTP 400
        }
    }

    /**
     * Update an existing product
     * PUT /api/products/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    /**
     * Delete a product
     * DELETE /api/products/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    /**
     * Search products by name
     * GET /api/products/search?name=apple
     * 
     * @RequestParam: Gets query parameters from URL
     */
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    /**
     * Get products in price range
     * GET /api/products/price-range?min=1.00&max=10.00
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsInPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        try {
            List<Product> products = productService.getProductsInPriceRange(min, max);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}