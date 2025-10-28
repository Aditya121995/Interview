package com.problems.inventoryManagement;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@ToString
public class Order {
    private final String orderId;
    private final User user;
    private final List<OrderItem> items;
    @Setter
    private OrderStatus status;
    private final LocalDateTime createdAt;
    @Setter
    private Payment payment;
    private final List<String> reservationIds;
    @Setter
    private double totalAmount;

    public Order(User user) {
        this.orderId = UUID.randomUUID().toString();
        this.user = user;
        this.items = new CopyOnWriteArrayList<>();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.reservationIds = new CopyOnWriteArrayList<>();
        this.totalAmount = 0.0;
        this.payment = null;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void addReservation(String reservationId) {
        reservationIds.add(reservationId);
    }
}
