package com.supermarket.checkout.web;

import org.springframework.web.bind.annotation.RestController;
import com.supermarket.checkout.service.OfferService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.supermarket.checkout.model.Offer;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/offers")
    public Iterable<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/offers/{id}")
    public Offer getOfferById(@PathVariable Long id) {
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found");
        }
        return offer;
    }

    @PostMapping("/offers")
    public Offer createOffer(@RequestBody Offer offer) {
        Offer saveOffer = offerService.saveOffer(offer);
        return saveOffer;
    }

    @DeleteMapping("/offers/{id}")
    public void deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
    }
}
