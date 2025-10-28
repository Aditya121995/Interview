package com.problems.inventoryManagement;

import org.aspectj.weaver.ast.Or;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService implements PaymentObserver {
    private final Map<String, Order> orders;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    public OrderService(PaymentService paymentService, InventoryService inventoryService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.orders = new ConcurrentHashMap<>();
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public Order createOrder(User user, PaymentMethod paymentMethod) {
        ShoppingCart cart = user.getCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
            System.out.println("Cart is empty");
            return null;
        }

        Order order = new Order(user);
        double totalAmount = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            InventoryReservation reservation = inventoryService
                    .reserveInventory(order.getOrderId(), cartItem.getProduct().getProductId(), cartItem.getQuantity());
            order.addReservation(reservation.getReservationId());

            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity(),
                    cartItem.getProduct().getProductPrice());
            order.addItem(orderItem);

            totalAmount += orderItem.getQuantity() * orderItem.getPrice();
        }

        order.setTotalAmount(totalAmount);

        Payment payment = paymentService.initiatePayment(user.getUserId(), order.getOrderId(),
                order.getTotalAmount(), paymentMethod);
        order.setPayment(payment);

        orders.put(order.getOrderId(),  order);
        user.addOrder(order);
        return order;
    }

    @Override
    public void onPaymentUpdate(Payment payment) {
        if (!orders.containsKey(payment.getOrderId())) {
            System.out.println("Order id " + payment.getOrderId() + " not found");
            return;
        }

        Order order = orders.get(payment.getOrderId());
        PaymentStatus status = payment.getStatus();
        if (PaymentStatus.SUCCESS.equals(status)) {
            // confirm all reservations
            for (String reservationId : order.getReservationIds()) {
                inventoryService.confirmReservation(reservationId);
            }

            order.setStatus(OrderStatus.CONFIRMED);
            order.getUser().getCart().clear();
            System.out.println("Order confirmed successfully");
        } else { // failed
            // release all reservations
            for (String reservationId : order.getReservationIds()) {
                inventoryService.releaseInventory(reservationId);
            }

            order.setStatus(OrderStatus.CANCELLED);
            System.out.println("Order cancelled because of payment failure");
        }
    }
}
