package com.problems.snakeAndLadder;

public class Ladder extends BoardElement {
    public Ladder(int start, int end) {
        super(start, end, BoardElementType.LADDER);
        if (start >= end) {
            throw new IllegalArgumentException("Ladder start must be less than end");
        }
    }
}
