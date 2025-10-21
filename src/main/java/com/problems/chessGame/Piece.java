package com.problems.chessGame;

import lombok.Getter;

@Getter
public abstract class Piece {
    private final Color color;
    private final boolean isAlive;

    Piece(Color color) {
        this.color = color;
        this.isAlive = true;
    }

    public abstract boolean canMove(Board board, Cell start, Cell end);
}
