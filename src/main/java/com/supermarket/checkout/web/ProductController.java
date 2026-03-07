package com.supermarket.checkout.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.service.ProductService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    // Initial test endpoint to verify the application is running
    @GetMapping("/")
    public String home() {

        return "Welcome to the Supermarket Checkout App!";

    }
  
    @GetMapping("/products")
    public Iterable<Product> getAllProducts() {

        return productService.getAllProducts();

    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {

        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return product;     

    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) { 

        Product saveProduct = productService.saveProduct(product);

        return saveProduct;
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

    }


}