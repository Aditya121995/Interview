package com.problems.chessGame;


public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        int direction = (this.getColor() == Color.WHITE) ? 1 : -1;
        int xDiff = end.getRow() - start.getRow();
        int yDiff = end.getCol() - start.getCol();

        boolean isFirstMove = (this.getColor() == Color.WHITE && start.getRow() == 1) ||
                (this.getColor() == Color.BLACK && start.getRow() == 6);

        // move forward
        if (yDiff == 0 && end.isEmpty()) {
            if (xDiff == direction) {
                return true;
            }

            if (isFirstMove && xDiff == 2*direction) {
                return true;
            }
        }

        // capture piece
        if (xDiff == direction && yDiff == 1 && !end.isEmpty()) {
            return this.getColor() != end.getPiece().getColor();
        }

        return false;
    }


}
