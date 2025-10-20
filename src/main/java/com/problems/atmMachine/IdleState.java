package com.problems.atmMachine;

public class IdleState implements AtmState {

    @Override
    public void insertCard(AtmSystem atmSystem, String cardNumber) {
        System.out.println("Inserting card " + cardNumber);
        Card card = atmSystem.getCard(cardNumber);
        if (card == null) {
            ejectCard(atmSystem);
        } else {
            atmSystem.setCurrentCard(card);
            atmSystem.setCurrentState(new CardInsertedState());
        }
    }

    @Override
    public void enterPin(AtmSystem atmSystem, String pin) {
        System.out.println("Please insert card first.");
    }

    @Override
    public void selectOperation(AtmSystem atmSystem, OperationType operationType, int amount) {
        System.out.println("Please insert card first.");
    }

    @Override
    public void ejectCard(AtmSystem atmSystem) {
        System.out.println("Invalid card. Card not found");
        atmSystem.setCurrentCard(null);
    }
}
