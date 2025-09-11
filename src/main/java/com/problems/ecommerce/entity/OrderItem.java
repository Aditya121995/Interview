package com.problems.ecommerce.entity;

import lombok.Data;

@Data
public class OrderItem {
    private final Product product;
    private int quantity;
    private final double price;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }
}
