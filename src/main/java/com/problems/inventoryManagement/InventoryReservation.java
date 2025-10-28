package com.problems.inventoryManagement;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class InventoryReservation {
    private final String reservationId;
    private final String productId;
    private final String orderId;
    @Setter
    private ReservationStatus status;
    private final int reservedQuantity;

    public InventoryReservation(String productId, String orderId, int reservedQuantity) {
        this.reservationId = UUID.randomUUID().toString();
        this.productId = productId;
        this.orderId = orderId;
        this.status = ReservationStatus.RESERVED;
        this.reservedQuantity = reservedQuantity;
    }
}
