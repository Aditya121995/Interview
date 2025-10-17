package com.problems.snakeAndLadder;

import lombok.Getter;

@Getter
public abstract class BoardElement {
    private final int start;
    private final int end;
    private final BoardElementType type;

    public BoardElement(int start, int end,  BoardElementType type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }
}
