package com.supermarket.checkout.service;
import com.supermarket.checkout.model.Cart;
import com.supermarket.checkout.model.CartItem;
import com.supermarket.checkout.model.Offer;
import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.repository.OfferRepository;
import com.supermarket.checkout.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;


@Service
public class CheckoutService {

    private final ProductService productService;
    private final OfferService offerService;

    public CheckoutService(ProductService productService, OfferService offerService) {
        this.productService = productService;
        this.offerService = offerService;
    }

public double calculateTotal(Cart cart) {

    cart = groupItemsByProduct(cart);
    double total = 0.0;

    for (CartItem item : cart.getItems()) {
        total += calculateItemSubtotal(item);
    }

    return total;
}


//Helper method: 1 Groupng items 
private Cart groupItemsByProduct(Cart cart) {

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

//Helper mthod: 2 Calculating subtotal for each item considering offers
private double calculateItemSubtotal(CartItem item) {
    
    Product product = productService.getProductById(item.getProductId());
    
    LocalDate today = LocalDate.now();
    Offer offer = offerService.getActiveOfferForProduct(item.getProductId(), today).orElse(null);
    
    if (offer != null && item.getQuantity() >= offer.getRequiredQuantity()) {
        int bundles = item.getQuantity() / offer.getRequiredQuantity();
        int remainder = item.getQuantity() % offer.getRequiredQuantity();
        
        return (bundles * offer.getBundlePrice()) + (remainder * product.getPrice());
    }
    
    return item.getQuantity() * product.getPrice();

}

}
    