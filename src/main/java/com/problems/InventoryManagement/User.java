package com.problems.InventoryManagement;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class User {
    private final String userId;
    private final String userName;
    private final String email;
    private final ShoppingCart cart;
    private final List<Order> orders;

    public User(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.cart = new ShoppingCart(userId);
        this.orders = new CopyOnWriteArrayList<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
