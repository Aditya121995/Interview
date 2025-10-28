package com.problems.inventoryManagement;

import lombok.Getter;

@Getter
public class OrderItem {
    private final Product product;
    private final int quantity;
    private final double price;

    public OrderItem(Product product, int quantity,  double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
