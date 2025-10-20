package com.problems.vendingMachine;

import lombok.Getter;
import lombok.Setter;

@Getter
public class VendingMachine {
    private final Inventory inventory;
    @Setter
    private VendingMachineState state;
    @Setter
    private Product selectedProduct;
    private double currentAmount;
    private double collectedAmount;

    public VendingMachine() {
        this.inventory = new Inventory();
        this.state = new IdleState();
        this.selectedProduct = null;
        this.currentAmount = 0.0;
        this.collectedAmount = 0.0;
    }

    public synchronized void restock(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.inventory.addProduct(product);
        }
    }

    public synchronized void selectProduct(String productCode) {
        state.selectProduct(this, productCode);
    }

    public synchronized void insertNote(Note note) {
        double value = note.getValue();
        state.insertMoney(this, value);
    }

    public synchronized void insertCoin(Coin coin) {
        double value = coin.getValue()/100.0;
        state.insertMoney(this, value);
    }

    public synchronized void dispenseProduct() {
        state.dispenseProduct(this);
    }

    public synchronized void cancelTransaction() {
        state.cancelTransaction(this);
    }

    public synchronized void collectMoney() {
        System.out.println("Collecting money :: " + collectedAmount);
        collectedAmount = 0;
    }

    // internal methods

    public void addMoney(double amount) {
        currentAmount += amount;
    }

    public void reset() {
        selectedProduct = null;
        currentAmount = 0.0;
        state = new IdleState();
    }

    public void collectMoney(double amount) {
        collectedAmount += amount;
    }
}
