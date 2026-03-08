package com.supermarket.checkout.repository;

import com.supermarket.checkout.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    // CRUD methods are automatically provided by CrudRepository
}
