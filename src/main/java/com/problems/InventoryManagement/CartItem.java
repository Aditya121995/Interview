package com.problems.InventoryManagement;

import lombok.Getter;

@Getter
public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void increaseQuantity(int qty) {
        this.quantity += qty;
    }

    public void decreaseQuantity(int qty) {
        if (quantity - qty >= 0) {
            this.quantity -= qty;
        }
    }
}
