package com.problems.snakeAndLadder;

public class Snake extends BoardElement {
    public Snake(int start, int end) {
        super(start,end, BoardElementType.SNAKE);
        if (start <= end) {
            throw new IllegalArgumentException("Snake start must be greater than end");
        }
    }
}
