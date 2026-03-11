package com.supermarket.checkout.service;
import com.supermarket.checkout.model.Cart;
import com.supermarket.checkout.model.CartItem;
import com.supermarket.checkout.model.Offer;
import com.supermarket.checkout.model.Product;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.math.BigDecimal;


@Service
public class CheckoutService {

    private final ProductService productService;
    private final OfferService offerService;

    public CheckoutService(ProductService productService, OfferService offerService) {
        this.productService = productService;
        this.offerService = offerService;
    }

    public BigDecimal calculateTotal(Cart cart) {

    cart = mergeCartItemsByProductId(cart);
    BigDecimal total = BigDecimal.ZERO;

    for (CartItem item : cart.getItems()) {
        total = total.add(calculateItemPriceWithOffers(item));  // total = priceCartItem[i] + total
    }

    return total;
}


//Helper method: 1 merging common items by adding their quantities
private Cart mergeCartItemsByProductId(Cart cart) {

    Map<Long, CartItem> groupedItems = new HashMap<>();

    for (CartItem item : cart.getItems()) {

        Long productId = item.getProductId();

        if (groupedItems.containsKey(productId)) {
            CartItem existingItem = groupedItems.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            groupedItems.put(productId, new CartItem(productId, item.getName(), item.getQuantity()));
        }
    }

    cart.setItems(new ArrayList<>(groupedItems.values()));

    return cart;
}


private BigDecimal calculateItemPriceWithOffers(CartItem item) {
    
    Product product = productService.getProductById(item.getProductId());
    
    LocalDate today = LocalDate.now();
    Offer offer = offerService.getActiveOfferForProduct(item.getProductId(), today).orElse(null);
    
    if (offer != null && item.getQuantity() >= offer.getRequiredBundleQuantity()) {

        int bundles = item.getQuantity() / offer.getRequiredBundleQuantity();
        int remainder = item.getQuantity() % offer.getRequiredBundleQuantity();
                   
        BigDecimal bundleTotal = offer.getBundlePrice().multiply(BigDecimal.valueOf(bundles));
        BigDecimal remainderTotal = product.getPrice().multiply(BigDecimal.valueOf(remainder));

        return bundleTotal.add(remainderTotal);
    }
    
    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

}

}
    