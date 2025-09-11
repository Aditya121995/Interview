package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void saveOrder(Order order);
    Optional<Order> findById(String id);
    List<Order> getAllOrders();
}
