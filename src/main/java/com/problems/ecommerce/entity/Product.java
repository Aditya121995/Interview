package com.problems.ecommerce.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Product {
    private final String id;
    private final String name;
    private final double price;
    private int inventory;

    public Product(String name, double price, int inventory) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.inventory = inventory;
    }

    public synchronized boolean reserveInventory(int quantity) {
        if (inventory >= quantity) {
            this.inventory -= quantity;
            return true;
        }

        return false;
    }

    public synchronized void releaseInventory(int quantity) {
        this.inventory += quantity;
    }
}
