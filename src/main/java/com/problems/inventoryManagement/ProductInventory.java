package com.problems.inventoryManagement;

import lombok.Getter;

@Getter
public class ProductInventory {
    private final String productId;
    private int totalQuantity;
    private int reservedQuantity;
    private int availableQuantity;

    public ProductInventory(String productId, int quantity) {
        this.productId = productId;
        this.totalQuantity = quantity;
        this.reservedQuantity = 0;
        this.availableQuantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.totalQuantity += quantity;
        this.availableQuantity += quantity;
    }

    public boolean reserveQuantity(int quantity) {
        if (quantity <= totalQuantity) {
            availableQuantity -= quantity;
            reservedQuantity += quantity;
            return true;
        }

        return false;
    }

    public void confirmReservation(int quantity) {
        totalQuantity -= quantity;
        reservedQuantity -= quantity;
    }

    public void releaseReservation(int quantity) {
        availableQuantity += quantity;
        reservedQuantity -= quantity;
    }
}
