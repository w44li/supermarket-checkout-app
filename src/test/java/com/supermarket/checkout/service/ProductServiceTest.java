package com.supermarket.checkout.service;

import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // Verify duplicate names are rejected (case-insensitive and trimmed)
    @Test
    void testSaveProduct_DuplicateNameThrowsException() {
        Product existing = new Product(10L, "Apple", new BigDecimal("1.00"));
        Product duplicate = new Product(null, "  apple  ", new BigDecimal("1.50"));

        when(productRepository.findAll()).thenReturn(Arrays.asList(existing));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.saveProduct(duplicate)
        );

        assertEquals("Product with name 'apple' already exists.", exception.getMessage());
        verify(productRepository, never()).save(duplicate);
    }

    // Verify updating same product id with same name is allowed
    @Test
    void testSaveProduct_UpdateSameProductNameAllowed() {
        Product existing = new Product(10L, "Apple", new BigDecimal("1.00"));
        Product update = new Product(10L, " apple ", new BigDecimal("1.20"));

        when(productRepository.findAll()).thenReturn(Arrays.asList(existing));
        when(productRepository.save(update)).thenReturn(new Product(10L, "apple", new BigDecimal("1.20")));

        Product saved = productService.saveProduct(update);

        assertEquals("apple", saved.getName());
        verify(productRepository).save(update);
    }
}
