package com.problems.snakeAndLadder;

import lombok.Getter;

import java.util.Random;

public class Dice {
    private final int count;
    private final int minValue;
    private final int maxValue;

    public Dice(int count, int minValue, int maxValue) {
        this.count = count;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int roll() {
        int finalNumber = 0;
        for (int i = 0; i < count; i++) {
            double randomNumber = Math.random();
            finalNumber += (int) (randomNumber*(maxValue - minValue + 1)) + minValue;
        }

        return finalNumber;
    }
}
