package com.problems.atmMachine;

public class AtmMachineDemo {
    public static void main(String[] args) {
        BankService bankService = new BankService();
        bankService.createAccount("1234566792");
        bankService.createAccount("2345872985");
        bankService.createCard("7384-9473-9573-6472", "1223", "1234566792");
        bankService.createCard("3578-9473-9573-6472", "5739", "2345872985");

        AtmSystem atmSystem = new AtmSystem(bankService);

        System.out.println("======== DEPOSIT ========");
        atmSystem.insertCard("7384-9473-9573-6472");
        atmSystem.enterPin("1223");
        atmSystem.selectOperation(OperationType.DEPOSIT, 10000);

        System.out.println("======== CHECK BALANCE ========");
        atmSystem.insertCard("7384-9473-9573-6472");
        atmSystem.enterPin("1223");
        atmSystem.selectOperation(OperationType.BALANCE_ENQUIRY, 0);

        System.out.println("======== Withdrawal ========");
        atmSystem.insertCard("7384-9473-9573-6472");
        atmSystem.enterPin("1223");
        atmSystem.selectOperation(OperationType.WITHDRAW, 830);

        System.out.println("======== Withdrawal ========");
        atmSystem.insertCard("7384-9473-9573-6472");
        atmSystem.enterPin("1223");
        atmSystem.selectOperation(OperationType.WITHDRAW, 1000);

        System.out.println("======== Withdrawal ========");
        atmSystem.insertCard("7384-9473-9573-6472");
        atmSystem.enterPin("1223");
        atmSystem.selectOperation(OperationType.WITHDRAW, 500);

        System.out.println("======== Withdrawal ========");
        atmSystem.insertCard("7384-9473-9573-6472");
        atmSystem.enterPin("1223");
        atmSystem.selectOperation(OperationType.WITHDRAW, 100);
    }
}
