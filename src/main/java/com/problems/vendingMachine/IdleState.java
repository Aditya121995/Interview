package com.problems.vendingMachine;

public class IdleState implements VendingMachineState {
    @Override
    public void selectProduct(VendingMachine machine, String productCode) {
        Product product = machine.getInventory().getProduct(productCode);
        if (product == null) {
            System.out.println("Product not found");
            return;
        }

        if (!machine.getInventory().hasProduct(productCode)) {
            System.out.println("Product is out of stock");
            return;
        }

        machine.setSelectedProduct(product);
        machine.setState(new ProductSelectedState());
        System.out.println("Product :: " + product.getName() +
                " is selected. Price of product :: " + product.getPrice());

    }

    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Please Select Product First");
    }

    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Please Select Product First");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("No Transaction present to cancel");
    }
}
