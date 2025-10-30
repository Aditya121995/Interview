package com.problems.InventoryManagement;

import lombok.Getter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
public class ProductInventory {
    private final String productId;
    private int totalQuantity;
    private int reservedQuantity;
    private int availableQuantity;
    private final ReentrantLock lock;

    public ProductInventory(String productId, int quantity) {
        this.productId = productId;
        this.totalQuantity = quantity;
        this.reservedQuantity = 0;
        this.availableQuantity = quantity;
        this.lock = new ReentrantLock();
    }

    public void addQuantity(int quantity) {
        lock.lock();
        try {
            this.totalQuantity += quantity;
            this.availableQuantity += quantity;
        } finally {
            lock.unlock();
        }
    }

    public boolean reserveQuantity(int quantity) {
        lock.lock();
        try {
            if (quantity <= availableQuantity) {
                availableQuantity -= quantity;
                reservedQuantity += quantity;
                return true;
            }

            return false;
        } finally {
            lock.unlock();
        }
    }

    public void confirmReservation(int quantity) {
        lock.lock();
        try {
            totalQuantity -= quantity;
            reservedQuantity -= quantity;
        } finally {
            lock.unlock();
        }
    }

    public void releaseReservation(int quantity) {
        lock.lock();
        try {
            availableQuantity += quantity;
            reservedQuantity -= quantity;
        } finally {
            lock.unlock();
        }
    }
}
