package com.problems.inventoryManagement;



import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InventoryService {
    private final Map<String, ProductInventory> inventoryMap;
    private final Map<String, InventoryReservation> reservationMap;

    public InventoryService() {
        this.inventoryMap = new ConcurrentHashMap<>();
        this.reservationMap = new ConcurrentHashMap<>();
    }

    public void addInventory(String productId, int quantity) {
        if (inventoryMap.containsKey(productId)) {
            ProductInventory productInventory = inventoryMap.get(productId);
            productInventory.addQuantity(quantity);
        } else {
            ProductInventory productInventory = new ProductInventory(productId, quantity);
            inventoryMap.put(productId, productInventory);
        }
    }

    public InventoryReservation reserveInventory(String orderId, String productId, int reservationQuantity) {
        if (!inventoryMap.containsKey(productId)) {
            // throw productNotFoundException
            System.out.println("Product " + productId + " not found");
            return null;
        }

        ProductInventory inventory = inventoryMap.get(productId);

        if (inventory.reserveQuantity(reservationQuantity)) {
            System.out.println("Reservation for product " + productId + " success. Available quantity is " + inventory.getAvailableQuantity());
            InventoryReservation inventoryReservation = new InventoryReservation(productId, orderId, reservationQuantity);
            reservationMap.put(inventoryReservation.getReservationId(), inventoryReservation);
            return inventoryReservation;
        }

        System.out.println("Not enough stock for product " + productId);
        return null;
    }

    public void confirmReservation(String reservationId) {
        InventoryReservation inventoryReservation = reservationMap.get(reservationId);
        if (inventoryReservation != null && inventoryReservation.getStatus().equals(ReservationStatus.RESERVED)) {
            ProductInventory inventory = inventoryMap.get(inventoryReservation.getProductId());
            if (inventory != null) {
                inventory.confirmReservation(inventoryReservation.getReservedQuantity());
                inventoryReservation.setStatus(ReservationStatus.CONFIRMED);
                System.out.println("Inventory confirmed for product " + inventoryReservation.getProductId());
            }
        }
    }

    public void releaseInventory(String reservationId) {
        InventoryReservation inventoryReservation = reservationMap.get(reservationId);
        if (inventoryReservation != null && inventoryReservation.getStatus().equals(ReservationStatus.RESERVED)) {
            ProductInventory inventory = inventoryMap.get(inventoryReservation.getProductId());
            if (inventory != null) {
                inventory.releaseReservation(inventoryReservation.getReservedQuantity());
                inventoryReservation.setStatus(ReservationStatus.RELEASED);
                System.out.println("Inventory released for product " + inventoryReservation.getProductId());
            }
        }
    }

    public List<InventoryReservation> getExpiredReservations() {
        return reservationMap.values().stream().filter(InventoryReservation::isExpired).collect(Collectors.toList());
    }
}
