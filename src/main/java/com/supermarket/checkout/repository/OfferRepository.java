package com.supermarket.checkout.repository;

import com.supermarket.checkout.model.Offer;
import org.springframework.data.repository.CrudRepository;

public interface OfferRepository extends CrudRepository<Offer, Long> {
    // CRUD methods are automatically provided by CrudRepository
}