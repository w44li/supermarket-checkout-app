package com.supermarket.checkout.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.supermarket.checkout.model.Cart;
import com.supermarket.checkout.service.CheckoutService;
import java.math.BigDecimal;

@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/cart/checkout")
    public BigDecimal checkout(@RequestBody Cart cart ) {

        return checkoutService.calculateTotal(cart);
    }
}