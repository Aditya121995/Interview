package com.problems.chessGame;


import java.util.Map;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        int xDiff = Math.abs(end.getRow() - start.getRow());
        int yDiff = Math.abs(end.getCol() - start.getCol());

        if ((xDiff == 1 && yDiff == 2) ||
                (yDiff == 1 && xDiff == 2)) {
            return true;
        }

        return false;
    }
}
