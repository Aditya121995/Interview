package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepositoryImpl implements OrderRepository {
    private final Map<String, Order> orders;

    public OrderRepositoryImpl() {
        this.orders = new ConcurrentHashMap<>();
    }

    @Override
    public void saveOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orders.values().stream().toList();
    }
}
