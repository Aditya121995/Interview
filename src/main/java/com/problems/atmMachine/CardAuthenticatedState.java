package com.problems.atmMachine;

public class CardAuthenticatedState implements AtmState{
    @Override
    public void insertCard(AtmSystem atmSystem, String cardNumber) {
        System.out.println("A Card is already inserted. Cannot insert another card");
    }

    @Override
    public void enterPin(AtmSystem atmSystem, String pin) {
        System.out.println("Pin has already been provide and authenticated");
    }

    @Override
    public void selectOperation(AtmSystem atmSystem, OperationType operationType, int amount) {
        switch (operationType) {
            case BALANCE_ENQUIRY:
                atmSystem.checkBalance();
                break;
            case DEPOSIT:
                System.out.println("Processing Deposit of amount " + amount);
                atmSystem.depositCash(amount);
                break;
            case WITHDRAW:
                System.out.println("Processing Withdraw of amount " + amount);
                atmSystem.withdrawCash(amount);
                break;
            default:
                System.out.println("Invalid Operation selected");
                break;
        }

        System.out.println("Operation has been successful");
        ejectCard(atmSystem);
    }

    @Override
    public void ejectCard(AtmSystem atmSystem) {
        System.out.println("Ending Session. Card Has been ejected. Thank you for using ATM");
        atmSystem.setCurrentCard(null);
        atmSystem.setCurrentState(new IdleState());
    }
}
