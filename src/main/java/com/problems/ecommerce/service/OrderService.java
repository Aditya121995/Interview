package com.problems.ecommerce.service;

import com.problems.ecommerce.entity.*;
import com.problems.ecommerce.enums.OrderStatus;
import com.problems.ecommerce.enums.PaymentMode;
import com.problems.ecommerce.repository.OrderRepository;
import com.problems.ecommerce.repository.OrderRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CustomerService customerService;
    private final PincodeServiceabilityService pincodeServiceabilityService;

    public OrderService(CartService cartService,
                        CustomerService customerService,
                        PincodeServiceabilityService pincodeServiceabilityService) {
        this.orderRepository = new OrderRepositoryImpl();
        this.cartService = cartService;
        this.customerService = customerService;
        this.pincodeServiceabilityService = pincodeServiceabilityService;
    }

    public String placeOrder(String customerId, PaymentMode paymentMode, String pinCode) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return null;
        }

        Cart cart = cartService.getCart(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            System.out.println("Cart not found");
            return null;
        }

        if (!pincodeServiceabilityService.isServiceable(pinCode)) {
            System.out.println("Pincode not serviceable");
            return null;
        }

        if (!pincodeServiceabilityService.getSupportedPaymentMethods(pinCode).contains(paymentMode)) {
            System.out.println("Payment method not supported");
            return null;
        }

        Order order = new Order(customerId);
        order.setPaymentMode(paymentMode);
        order.setDeliveryPincode(pinCode);

        for (CartItem item : cart.getItems()) {
            order.addOrderItem(new OrderItem(item.getProduct(), item.getQuantity()));
        }

        orderRepository.saveOrder(order);
        System.out.println("Order placed successfully");
        return order.getOrderId();
    }

    public boolean confirmOrder(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found");
            return false;
        }
        Order order = orderOpt.get();

        synchronized (order) {
            if (order.getOrderStatus() ==  OrderStatus.CREATED) {
                order.setOrderStatus(OrderStatus.CONFIRMED);
                cartService.clearCart(order.getCustomerId());
                orderRepository.saveOrder(order);
                System.out.println("Order confirmed successfully");
                return true;
            }
        }

        System.out.println("Order not confirmed successfully");
        return false;
    }

    public boolean cancelOrder(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found");
            return false;
        }
        Order order = orderOpt.get();
        synchronized (order) {
            if (order.getOrderStatus() == OrderStatus.CREATED) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                orderRepository.saveOrder(order);
                System.out.println("Order cancelled successfully");
                return true;
            }
        }
        System.out.println("Order not cancelled successfully");
        return false;
    }

    public Order getOrder(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found");
            return null;
        }
        return orderOpt.get();
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
}
