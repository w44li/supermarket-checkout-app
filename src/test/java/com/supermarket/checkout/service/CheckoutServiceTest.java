package com.supermarket.checkout.service;

import com.supermarket.checkout.model.Cart;
import com.supermarket.checkout.model.CartItem;
import com.supermarket.checkout.model.Offer;
import com.supermarket.checkout.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private CheckoutService checkoutService;

    private Product appleProduct, bananaProduct;

    @BeforeEach
    void setUp() {
        appleProduct = new Product(1L, "Apple", new BigDecimal("1.00"));
        bananaProduct = new Product(2L, "Banana", new BigDecimal("0.50"));
    }

    // Verify total calculation when no active offers exist
    @Test
    void testCalculateTotal_NoOffers() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 3),
                new CartItem(2L, "Banana", 2)
        ));

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(productService.getProductById(2L)).thenReturn(bananaProduct);
        
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(offerService.getActiveOfferForProduct(eq(2L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("4.00"), total);
    }

    // Verify bundle pricing when quantity exactly matches offer requirement
    @Test
    void testCalculateTotal_WithExactBundle() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 3)
        ));

        Offer offer = new Offer(
                1L,
                1L,
                3,
                new BigDecimal("2.50"),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.of(offer));

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("2.50"), total);
    }

    // Verify bundle pricing with remainder items charged at regular price
    @Test
    void testCalculateTotal_WithBundleAndRemainder() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 7)
        ));

        Offer offer = new Offer(
                1L, 1L, 3, new BigDecimal("2.50"),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.of(offer));

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("6.00"), total);
    }

    // Verify duplicate product items have correct price regardless of order
    @Test
    void testCalculateTotal_OrderDoesNotAffectTotal() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 2),
                new CartItem(2L, "Banana", 2),
                new CartItem(1L, "Apple", 3)
        ));

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(productService.getProductById(2L)).thenReturn(bananaProduct);
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(offerService.getActiveOfferForProduct(eq(2L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("6.00"), total);
    }

    // Verify regular pricing when quantity is below offer threshold
    @Test
    void testCalculateTotal_BelowOfferThreshold() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 2)
        ));

        Offer offer = new Offer(
                1L, 1L, 3, new BigDecimal("2.50"),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.of(offer));

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("2.00"), total);
    }

    // Verify total calculation with multiple products and mixed offers
    @Test
    void testCalculateTotal_MultipleProducts() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 3),
                new CartItem(2L, "Banana", 2)
        ));

        Offer appleOffer = new Offer(
                1L, 1L, 3, new BigDecimal("2.50"),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(productService.getProductById(2L)).thenReturn(bananaProduct);
        
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.of(appleOffer));
        when(offerService.getActiveOfferForProduct(eq(2L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("3.50"), total);
    }

    // Verify regular pricing when offer is expired
    @Test
    void testCalculateTotal_ExpiredOffer() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 3)
        ));

        when(productService.getProductById(1L)).thenReturn(appleProduct);
        when(offerService.getActiveOfferForProduct(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        BigDecimal total = checkoutService.calculateTotal(cart);

        assertEquals(new BigDecimal("3.00"), total);
    }
}
