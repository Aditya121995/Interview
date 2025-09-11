package com.problems.ecommerce.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Data
public class Cart {
    private final String customerId;
    private List<CartItem> items;

    public Cart(String customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public synchronized boolean addItem(Product product, int quantity) {
        if (product.reserveInventory(quantity)) {
             Optional<CartItem> existingItemOpt = fetchCartItem(product);
             if (existingItemOpt.isEmpty()) {
                 CartItem cartItem = new CartItem(product, quantity);
                 items.add(cartItem);
             } else {
                 CartItem existingItem = existingItemOpt.get();
                 existingItem.updateCartItem(quantity);
             }

             return true;
        }
        return false;
    }

    private Optional<CartItem> fetchCartItem(Product product) {
        return items.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId())).findFirst();
    }

    public synchronized void removeExpiredItems() {
        Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            CartItem item = it.next();
            if (item.isExpired()) {
                item.getProduct().releaseInventory(item.getQuantity());
                it.remove(); // safe removal
            }
        }
    }

    public synchronized void clear() {
        items.clear();
    }

    public synchronized double calculateTotal() {
        removeExpiredItems();
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice()*item.getQuantity())
                .sum();
    }


}
