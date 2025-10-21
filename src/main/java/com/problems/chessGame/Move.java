package com.problems.chessGame;

import lombok.Getter;

@Getter
public class Move {
    private final Player player;
    private final Cell start;
    private final Cell end;
    private final Piece pieceMoved;
    private final Piece pieceCaptured;

    public Move(Player player, Cell start, Cell end) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
        this.pieceCaptured = end.getPiece();
    }
}
