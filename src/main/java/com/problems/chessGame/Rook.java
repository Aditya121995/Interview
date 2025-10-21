package com.problems.chessGame;


public class Rook extends Piece{
    public Rook(Color color){
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        if (start.getRow() != end.getRow() && start.getCol() != end.getCol()) {
            return false;
        }

        return board.isPathClear(start, end);
    }
}
