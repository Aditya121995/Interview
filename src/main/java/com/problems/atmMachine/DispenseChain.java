package com.problems.atmMachine;

public interface DispenseChain {
    void setNextChain(DispenseChain dispenseChain);
    void dispense(int amount);
    boolean canDispense(int amount);
}
