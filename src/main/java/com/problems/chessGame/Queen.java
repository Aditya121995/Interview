package com.problems.chessGame;


public class Queen extends Piece{
    public Queen(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        int xDiff = Math.abs(end.getRow() - start.getRow());
        int yDiff = Math.abs(end.getCol() - start.getCol());

        if (xDiff != yDiff &&
                (start.getRow() != end.getRow() && start.getCol() != end.getCol())) {
            return false;
        }

        return board.isPathClear(start, end);
    }


}
