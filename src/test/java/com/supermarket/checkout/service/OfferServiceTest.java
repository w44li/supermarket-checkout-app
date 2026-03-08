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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
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

        when(offerRepository.findAll()).thenReturn(Collections.emptyList());
        when(offerRepository.save(offer)).thenReturn(persisted);

        Offer saved = offerService.saveOffer(offer);

        assertEquals(100L, saved.getId());
        verify(offerRepository).save(offer);
    }

        // Verify overlapping offers for the same product are rejected
    @Test
        void testSaveOffer_OverlappingDateRangeThrowsException() {
        Offer existing = new Offer(
                10L,
                1L,
                2,
                new BigDecimal("0.45"),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7)
        );

        Offer duplicate = new Offer(
                null,
                1L,
                3,
                new BigDecimal("2.50"),
                LocalDate.of(2026, 3, 5),
                LocalDate.of(2026, 3, 12)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(existing));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> offerService.saveOffer(duplicate)
        );

        assertEquals(
                "An overlapping offer already exists for this product.",
                exception.getMessage()
        );
        verify(offerRepository, never()).save(duplicate);
    }

    // Verify a new offer is allowed for the same product when the previous one is expired/non-overlapping
    @Test
    void testSaveOffer_ExpiredOfferAllowsNewOffer() {
        Offer expired = new Offer(
                10L,
                1L,
                2,
                new BigDecimal("0.45"),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7)
        );

        Offer newOffer = new Offer(
                null,
                1L,
                3,
                new BigDecimal("2.50"),
                LocalDate.of(2026, 3, 8),
                LocalDate.of(2026, 3, 14)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(expired));
        when(offerRepository.save(newOffer)).thenReturn(newOffer);

        Offer saved = offerService.saveOffer(newOffer);

        assertEquals(1L, saved.getProductId());
        assertEquals(3, saved.getRequiredQuantity());
        verify(offerRepository).save(newOffer);
    }

    // Verify saving the same offer id is allowed
    @Test
    void testSaveOffer_UpdateSameOfferAllowed() {
        Offer existing = new Offer(
                10L,
                1L,
                2,
                new BigDecimal("0.45"),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7)
        );

        Offer update = new Offer(
                10L,
                1L,
                3,
                new BigDecimal("2.50"),
                LocalDate.of(2026, 3, 8),
                LocalDate.of(2026, 3, 14)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(existing));
        when(offerRepository.save(update)).thenReturn(update);

        Offer saved = offerService.saveOffer(update);

        assertEquals(10L, saved.getId());
        assertEquals(3, saved.getRequiredQuantity());
        verify(offerRepository).save(update);
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

    // Verify start and end dates are treated as inclusive (boundary condition)
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

    // Verify an expired offer is ignored when a newer active offer exists for the same product
    @Test
    void testGetActiveOfferForProduct_IgnoresExpiredOfferAndReturnsCurrentOffer() {
        Offer expired = new Offer(
                3L,
                1L,
                2,
                new BigDecimal("0.45"),
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 7)
        );

        Offer current = new Offer(
                4L,
                1L,
                3,
                new BigDecimal("2.50"),
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(expired, current));

        Optional<Offer> result = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 5));

        assertTrue(result.isPresent());
        assertEquals(current.getId(), result.get().getId());
    }

    // Verify a future offer is not applied before its start date
    @Test
    void testGetActiveOfferForProduct_FutureOfferIsIgnoredBeforeStartDate() {
        Offer future = new Offer(
                5L,
                1L,
                3,
                new BigDecimal("2.50"),
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 7)
        );

        when(offerRepository.findAll()).thenReturn(Arrays.asList(future));

        Optional<Offer> result = offerService.getActiveOfferForProduct(1L, LocalDate.of(2026, 3, 31));

        assertFalse(result.isPresent());
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
