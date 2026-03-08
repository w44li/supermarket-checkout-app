package com.supermarket.checkout.service;
import org.springframework.stereotype.Service;

import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.repository.ProductRepository;

import java.util.stream.StreamSupport;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        String normalizedName = product.getName() == null ? "" : product.getName().trim();

        Product existingProductWithSameName = StreamSupport
                .stream(productRepository.findAll().spliterator(), false)
                .filter(existing -> existing.getName() != null
                        && existing.getName().trim().equalsIgnoreCase(normalizedName))
                .findFirst()
                .orElse(null);

        if (existingProductWithSameName != null
                && (product.getId() == null || !existingProductWithSameName.getId().equals(product.getId()))) {
            throw new IllegalArgumentException("Product with name '" + normalizedName + "' already exists.");
        }

        product.setName(normalizedName);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public long getProductCount() {
        return productRepository.count(); 
    }
}
