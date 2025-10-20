package com.problems.vendingMachine;

public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();

        Product coke = new Product("p1", "COKE", 10.0);
        Product pepsi = new Product("p2", "PEPSI", 20.0);
        Product soda = new Product("p3", "SODA", 5.0);

        machine.restock(coke, 2);
        machine.restock(pepsi, 10);
        machine.restock(soda, 10);

        System.out.println("=========Transaction 1===========");
        machine.selectProduct("p1");
        machine.insertNote(Note.FIFTY);

        System.out.println("=========Transaction 2===========");
        machine.selectProduct("p1");
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.insertNote(Note.HUNDRED);
        machine.cancelTransaction();

        System.out.println("=========Transaction 3===========");
        machine.selectProduct("p1");

        System.out.println("=========Transaction 1===========");
        machine.selectProduct("p2");
        machine.cancelTransaction();
    }
}
