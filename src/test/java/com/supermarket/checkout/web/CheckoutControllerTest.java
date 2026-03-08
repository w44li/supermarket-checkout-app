package com.supermarket.checkout.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.checkout.model.Cart;
import com.supermarket.checkout.model.CartItem;
import com.supermarket.checkout.service.CheckoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CheckoutService checkoutService;

    // Verify valid cart request returns HTTP 200 with correct total
    @Test
    void testCheckout_ValidCart_Returns200() throws Exception {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 3),
                new CartItem(2L, "Banana", 2)
        ));

        when(checkoutService.calculateTotal(any(Cart.class)))
                .thenReturn(new BigDecimal("5.50"));

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(content().string("5.50"));
    }

    // Verify negative quantity returns HTTP 400 validation error
    @Test
    void testCheckout_NegativeQuantity_Returns400() throws Exception {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", -3)
        ));

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"));
    }

    // Verify missing required field returns HTTP 400
    @Test
    void testCheckout_MissingProductId_Returns400() throws Exception {
        String invalidJson = """
                {
                  "items": [
                    {
                      "name": "Apple",
                      "quantity": 3
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    // Verify empty cart returns HTTP 200 with zero total
    @Test
    void testCheckout_EmptyCart_Returns200() throws Exception {
        Cart cart = new Cart();
        cart.setItems(Collections.emptyList());

        when(checkoutService.calculateTotal(any(Cart.class)))
                .thenReturn(BigDecimal.ZERO);

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    // Verify malformed JSON returns HTTP 400
    @Test
    void testCheckout_MalformedJson_Returns400() throws Exception {
        String malformedJson = "{ \"items\": [ ";

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Malformed request body"));
    }

    // Verify missing name field returns HTTP 400
    @Test
    void testCheckout_MissingName_Returns400() throws Exception {
        String jsonWithoutName = """
                {
                  "items": [
                    {
                      "productId": 1,
                      "quantity": 3
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithoutName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"));
    }

    // Verify checkout with duplicate products returns HTTP 200
    @Test
    void testCheckout_DuplicateProducts_Returns200() throws Exception {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList(
                new CartItem(1L, "Apple", 2),
                new CartItem(1L, "Apple", 3)
        ));

        when(checkoutService.calculateTotal(any(Cart.class)))
                .thenReturn(new BigDecimal("5.00"));

        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(content().string("5.00"));
    }
}
