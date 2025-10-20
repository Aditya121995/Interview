package com.problems.atmMachine;

public class CardInsertedState implements AtmState {
    @Override
    public void insertCard(AtmSystem atmSystem, String cardNumber) {
        System.out.println("A Card is already inserted. Cannot insert another card");
    }

    @Override
    public void enterPin(AtmSystem atmSystem, String pin) {
        System.out.println("Entered pin. Athenticating now");
        boolean isAuthenticated = atmSystem.authenticateCard(pin);

        if (isAuthenticated) {
            System.out.println("Card has been authenticated");
            atmSystem.setCurrentState(new CardAuthenticatedState());
        } else {
            System.out.println("Invalid Pin. Authentication failed");
            ejectCard(atmSystem);
        }
    }

    @Override
    public void selectOperation(AtmSystem atmSystem, OperationType operationType, int amount) {
        System.out.println("Enter pin to select operation");
    }

    @Override
    public void ejectCard(AtmSystem atmSystem) {
        System.out.println("Card Has been ejected. Thank you for using ATM");
        atmSystem.setCurrentCard(null);
        atmSystem.setCurrentState(new IdleState());
    }
}
