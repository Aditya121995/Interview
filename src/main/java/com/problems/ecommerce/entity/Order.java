package com.problems.ecommerce.entity;

import com.problems.ecommerce.enums.OrderStatus;
import com.problems.ecommerce.enums.PaymentMode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Order {
    private final String orderId;
    private final String customerId;
    private final List<OrderItem> orderItems;
    private double totalAmount;
    private PaymentMode paymentMode;
    private String deliveryPincode;
    private final Long createdAt;
    private OrderStatus orderStatus;

    public Order(String customerId) {
        this.customerId=customerId;
        this.orderId= UUID.randomUUID().toString();
        this.orderItems=new ArrayList<>();
        this.totalAmount=0.0;
        this.createdAt=System.currentTimeMillis();
        this.orderStatus=OrderStatus.CREATED;
    }

    public void addOrderItem(OrderItem item){
        this.orderItems.add(item);
        calculateTotal();
    }

    public void calculateTotal() {
        totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice()*item.getQuantity())
                .sum();
    }

}
