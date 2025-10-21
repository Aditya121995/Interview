package com.problems.chessGame;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Player {
    private final String name;
    private final Color color;
    private final List<Piece> pieces;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.pieces = new ArrayList<>();
    }

    public void addPiece(Piece p) {
        this.pieces.add(p);
    }

    public void removePiece(Piece p) {
        this.pieces.remove(p);
    }
}
