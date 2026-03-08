package com.supermarket.checkout.service;

import com.supermarket.checkout.model.Offer;
import com.supermarket.checkout.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    // Verify save delegates to repository
    @Test
    void testSaveOffer_DelegatesToRepository() {
        Offer offer = new Offer(
                null,
                1L,
                2,
                new BigDecimal("0.45"),
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        Offer persisted = new Offer(
                100L,
                1L,
                2,
                new BigDecimal("0.45"),
                offer.getStartDate(),
                offer.getEndDate()
        );

        when(offerRepository.save(offer)).thenReturn(persisted);

        Offer saved = offerService.saveOffer(offer);

        assertEquals(100L, saved.getId());
        verify(offerRepository).save(offer);
    }

    // Verify active offer is returned when date is within range
    @Test
    void testGetActiveOfferForProduct_DateWithinRange() {
        Offer active = new Offer(
                1L,
                1L,
                2,
                new BigDecimal("0.45"),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(active));

        Optional<Offer> result = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 5));

        assertTrue(result.isPresent());
        assertEquals(active.getId(), result.get().getId());
    }

    // Verify start and end dates are treated as inclusive
    @Test
    void testGetActiveOfferForProduct_DateBoundariesInclusive() {
        Offer active = new Offer(
                2L,
                1L,
                3,
                new BigDecimal("0.60"),
                LocalDate.of(2026, 3, 10),
                LocalDate.of(2026, 3, 20)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(active));

        Optional<Offer> atStart = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 10));
        Optional<Offer> atEnd = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 20));

        assertTrue(atStart.isPresent());
        assertTrue(atEnd.isPresent());
    }

    // Verify no offer is returned when date is outside range or product differs
    @Test
    void testGetActiveOfferForProduct_NoMatch() {
        Offer offer = new Offer(
                3L,
                2L,
                2,
                new BigDecimal("1.00"),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(offer));

        Optional<Offer> outsideDate = offerService.getActiveOfferForProduct(2L, LocalDate.of(2026, 3, 8));
        Optional<Offer> wrongProduct = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 5));
        when(offerRepository.findAll()).thenReturn(Collections.emptyList());
        Optional<Offer> emptyRepo = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 5));

        assertFalse(outsideDate.isPresent());
        assertFalse(wrongProduct.isPresent());
        assertFalse(emptyRepo.isPresent());
    }
}
