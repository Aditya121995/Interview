package com.problems.vendingMachine;

public class ProductSelectedState implements VendingMachineState {
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        System.out.println("Product :: " + machine.getSelectedProduct() + " is already selected");
    }

    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Inserting money ... ");

        machine.addMoney(amount);
        System.out.println("Money Inserted :: " + amount);
        System.out.println("Total Money :: " + machine.getCurrentAmount());

        if (machine.getCurrentAmount() >= machine.getSelectedProduct().getPrice()) {
            machine.setState(new MoneyInsertedState());
            machine.dispenseProduct();
        } else {
            double remaining =  machine.getSelectedProduct().getPrice() - machine.getCurrentAmount();
            System.out.println("Please insert remaining amount :: " + remaining);
        }
    }

    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Money needs to be inserted first.");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        machine.reset();
        System.out.println("Transaction has been cancelled.");
    }
}
