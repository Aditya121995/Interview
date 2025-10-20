package com.problems.vendingMachine;

public class MoneyInsertedState implements VendingMachineState {
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        System.out.println("Already Processing a transaction");
    }

    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        machine.addMoney(amount);
        System.out.println("Additional amount is inserted :: " + amount);
    }

    @Override
    public void dispenseProduct(VendingMachine machine) {
        Product product = machine.getSelectedProduct();
        double change = machine.getCurrentAmount() - product.getPrice();

        machine.getInventory().reduceProduct(product);
        machine.collectMoney(product.getPrice());

        System.out.println("Dispensing product :: " + product.getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (change > 0) {
            System.out.println("Returning Change :: " + change);
        }

        machine.reset();
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Already Processing a transaction");
    }
}
