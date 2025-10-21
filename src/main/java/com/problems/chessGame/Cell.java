package com.problems.chessGame;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {
    private final int row;
    private final int col;
    @Setter
    private Piece piece;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isEmpty() {
        return piece == null;
    }
}
