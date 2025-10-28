package com.problems.inventoryManagement;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderExpirationService {
    private final OrderService orderService;
    private final InventoryService inventoryService;
    private final ScheduledExecutorService scheduler;
    private final int CHECK_IN_SECONDS = 5;

    public OrderExpirationService(OrderService orderService,  InventoryService inventoryService) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startExpirationMonitoring() {
        scheduler.scheduleAtFixedRate(this::checkAndExpiredOrders,
                CHECK_IN_SECONDS, CHECK_IN_SECONDS, TimeUnit.SECONDS);
        System.out.println("Order Expiration Service is started");
    }

    private void checkAndExpiredOrders() {
        List<InventoryReservation> expiredReservation = inventoryService.getExpiredReservations();

        if (expiredReservation.isEmpty()) {
            System.out.println("No Expired Order is found");
            return;
        }

        for (InventoryReservation ir : expiredReservation) {
            String orderId = ir.getOrderId();
            inventoryService.releaseInventory(ir.getReservationId());

            Order order = orderService.getOrder(orderId);
            if (order != null) {
                order.setStatus(OrderStatus.EXPIRED);

                if (order.getPayment() != null) {
                    order.getPayment().setStatus(PaymentStatus.FAILED);
                }
            }
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            };
            System.out.println("Order Expiration Service is shutdown");
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
