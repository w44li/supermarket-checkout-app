package com.supermarket.checkout.service;

import java.time.LocalDate;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.supermarket.checkout.model.Offer; 
import com.supermarket.checkout.repository.OfferRepository;

import java.util.Optional;

@Service
public class OfferService {

    // Inject the OfferRepository
    private final OfferRepository offerRepository;  

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Iterable<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }   

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    public long getOfferCount() {
        return offerRepository.count(); 
    }

    public Optional<Offer> getActiveOfferForProduct(Long productId, LocalDate date) {
    return StreamSupport.stream(offerRepository.findAll().spliterator(), false)
        .filter(offer -> offer.getProductId().equals(productId)
            && !date.isBefore(offer.getStartDate())
            && !date.isAfter(offer.getEndDate()))
        .findFirst();
}

    
}
