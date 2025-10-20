package com.problems.vendingMachine;

public interface VendingMachineState {
    void selectProduct(VendingMachine machine, String productCode);
    void insertMoney(VendingMachine machine, double amount);
    void dispenseProduct(VendingMachine machine);
    void cancelTransaction(VendingMachine machine);
}
