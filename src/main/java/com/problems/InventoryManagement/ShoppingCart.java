package com.problems.InventoryManagement;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ShoppingCart {
    private final String userId;
    private final List<CartItem> cartItems;

    public ShoppingCart(String userId) {
        this.userId = userId;
        this.cartItems = new CopyOnWriteArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.increaseQuantity(quantity);
                System.out.println("Increased quantity in cart, product " + product.getProductId() + " " + item.getQuantity());
                return;
            }
        }

        cartItems.add(new CartItem(product, quantity));
        System.out.println("Added to cart, product " + product.getProductId() + " " + quantity);
    }

    public void removeItem(String productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                cartItems.remove(item);
                break;
            }
        }
    }

    public void addQuantity(String productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.increaseQuantity(quantity);
                break;
            }
        }
    }

    public void removeQuantity(String productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.decreaseQuantity(quantity);
                if (item.getQuantity() == 0) {
                    cartItems.remove(item);
                }
                break;
            }
        }
    }

    public void clear() {
        cartItems.clear();
    }
}
