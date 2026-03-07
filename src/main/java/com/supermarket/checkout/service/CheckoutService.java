package com.supermarket.checkout.service;
import com.supermarket.checkout.model.Cart;
import com.supermarket.checkout.model.CartItem;
import com.supermarket.checkout.model.Offer;
import com.supermarket.checkout.model.Product;
import com.supermarket.checkout.repository.OfferRepository;
import com.supermarket.checkout.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;

    public CheckoutService(ProductRepository productRepository, OfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
    }

    public Double calculateTotal(Cart cart) {
        Double total = 0.0;

        for (CartItem item : cart.getItems()) {

            // FETCH PRODUCT DETAILS that match the productId in the cart item
            Optional<Product> productBought = productRepository.findById(item.getProductId());

            if (productBought.isPresent()) {
                Product product = productBought.get();
                Double itemTotal = product.getPrice().doubleValue() * item.getQuantity();

                // Check for applicable offers
                Iterable<Offer> offers = offerRepository.findAll();
                
                for (Offer offer : offers) {

                    if (offer.getProductId().equals(product.getId())) {

                        /*
                            we have a bundlPrice if there is an offer 
                            we need to iterate through the offers and check that if the product in the cart matches 
                            the productId, then we need to check if the quantity in the cart matches the offer quantity, if it does then we apply the bundle price
                            need to check for the edge case where multiple offers exist for the same product, we need to apply the best offer which is the one that gives the lowest price
                            Buisness Requirement: no matter the order and combination of items in the cart: 
                            Meaning if in cart we have 


                         */

                        if (item.getQuantity() >= offer.getRequiredQuantity()) {
                            int numberOfBundles = item.getQuantity() / offer.getRequiredQuantity();
                            int remainingItems = item.getQuantity() % offer.getRequiredQuantity();

                            Double offerTotal = numberOfBundles * offer.getBundlePrice() + remainingItems * product.getPrice().doubleValue();

                            // Apply the best offer (lowest price)
                            itemTotal = Math.min(itemTotal, offerTotal);
                    }
                }

                total += itemTotal;
            }
        }

            return total;
        }
    }


public Cart groupItemsByProduct(Cart cart) {

    Map<Long, CartItem> groupedItems = new HashMap<>();

    for (CartItem item : cart.getItems()) {

        Long productId = item.getProductId();

        if (groupedItems.containsKey(productId)) {
            CartItem existingItem = groupedItems.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            groupedItems.put(productId, new CartItem(productId, item.getQuantity()));
        }
    }

    cart.setItems(new ArrayList<>(groupedItems.values()));

    return cart;
}

    public Cart applyOffers(Cart cart) {
        // This method can be implemented to modify the cart items based on applicable offers
        // For example, it could add a field to CartItem to indicate the applied offer and the discounted price
        return cart;
    }




}
    