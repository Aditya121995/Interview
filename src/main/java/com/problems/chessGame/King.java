package com.problems.chessGame;


public class King extends Piece {
    public King(Color color) {
        super(color);
    }

    @Override
    public boolean canMove(Board board, Cell start, Cell end) {
        int xDiff = Math.abs(end.getRow() - start.getRow());
        int yDiff = Math.abs(end.getCol() - start.getCol());

        return xDiff <= 1 && yDiff <= 1 && (end.isEmpty() || end.getPiece().getColor() != this.getColor());
    }

}
