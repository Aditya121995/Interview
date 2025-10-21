package com.problems.chessGame;

public class ChessGameDemo {
    public static void main(String[] args) {
        Player white = new Player("Alice", Color.WHITE);
        Player black = new Player("Bob", Color.BLACK);

        ChessGame game = new ChessGame(white, black);

        if (game.makeMove(1, 4, 3, 4)) {
            System.out.println("Move successful");
        }

        if (game.makeMove(6, 4, 4, 4)) {
            System.out.println("Move successful");
        }

        if (game.makeMove(0, 1, 2, 2)) {
            System.out.println("Move successful");
        }

        if (game.makeMove(6, 2, 2, 3)) {
            System.out.println("Move successful");
        }
    }
}
