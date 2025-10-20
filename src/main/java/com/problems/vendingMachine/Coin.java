package com.problems.vendingMachine;

public enum Coin {
    PENNY(1),
    DIME(10),
    NICKLE(5),
    QUARTER(25);

    private int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
