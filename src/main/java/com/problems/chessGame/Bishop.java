package com.problems.chessGame;


public class Bishop extends Piece{
    public Bishop(Color color){
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        int xDiff = Math.abs(end.getRow() - start.getRow());
        int yDiff = Math.abs(end.getCol() - start.getCol());

        if (xDiff != yDiff) {
            return false;
        }

        return board.isPathClear(start, end);
    }


}
