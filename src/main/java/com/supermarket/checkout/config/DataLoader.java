package com.supermarket.checkout.config;

import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component // spring should knows it 
public class DataLoader implements CommandLineRunner {

    private final ProductService productService;

    public DataLoader(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        if (productService.getAllProducts().iterator().hasNext()) {
            return;
        }

        List<String> productNames = List.of(
            "Milk", "Bread", "Eggs", "Rice", "Pasta",
            "Cheese", "Butter", "Apples", "Bananas", "Coffee"
        );

        for (String name : productNames) {
            Product product = new Product();
            product.setName(name);
            product.setPrice(randomPrice(0.50, 50.00));

            productService.saveProduct(product);
        }
    }

    private double randomPrice(double min, double max) {
        double value = ThreadLocalRandom.current().nextDouble(min, max);
        return Math.round(value * 100.0) / 100.0;
    }
}