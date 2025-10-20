package com.problems.atmMachine;

public interface AtmState {
    void insertCard(AtmSystem atmSystem, String cardNumber);
    void enterPin(AtmSystem atmSystem, String pin);
    void selectOperation(AtmSystem atmSystem, OperationType operationType, int amount);
    void ejectCard(AtmSystem atmSystem);
}
