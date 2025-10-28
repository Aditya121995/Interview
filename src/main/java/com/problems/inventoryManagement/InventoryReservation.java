package com.problems.inventoryManagement;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InventoryReservation {
    private final String reservationId;
    private final String productId;
    private final String orderId;
    @Setter
    private ReservationStatus status;
    private final int reservedQuantity;
    private final LocalDateTime expiryTime;

    public InventoryReservation(String productId, String orderId, int reservedQuantity) {
        this.reservationId = UUID.randomUUID().toString();
        this.productId = productId;
        this.orderId = orderId;
        this.status = ReservationStatus.RESERVED;
        this.reservedQuantity = reservedQuantity;
        this.expiryTime = LocalDateTime.now().plusSeconds(15);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime) && status.equals(ReservationStatus.RESERVED);
    }
}
