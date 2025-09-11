package com.problems.ecommerce.entity;

import lombok.Data;

@Data
public class CartItem {
    private final Product product;
    private int quantity;
    private long expiryTime;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.expiryTime = System.currentTimeMillis() + 5*1000;
    }

    public synchronized void updateCartItem(int quantity) {
        this.quantity = quantity;
        this.expiryTime = System.currentTimeMillis() + 5*60*1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
