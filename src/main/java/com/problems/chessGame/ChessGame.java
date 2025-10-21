package com.problems.chessGame;

import java.awt.font.GlyphMetrics;
import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private GameStatus status;
    private final List<Move> movesHistory;

    public ChessGame(Player p1, Player p2) {
        this.board = Board.getInstance();
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1; // player 1 is white player
        this.status = GameStatus.ACTIVE;
        this.movesHistory = new ArrayList<>();
    }

    public boolean makeMove(int startX, int startY, int endX, int endY) {
        Cell start = board.getCell(startX, startY);
        Cell end = board.getCell(endX, endY);

        Piece pieceToMove = start.getPiece();

        if (pieceToMove == null) {
            System.out.println("Invalid move. Does not have any piece.");
            return false;
        }

        if (pieceToMove.getColor() != currentPlayer.getColor()) {
            System.out.println("Invalid move. Not your piece to move.");
            return false;
        }

        if (!pieceToMove.canMove(board, start, end)) {
            System.out.println("Invalid move.");
            return false;
        }

        if (causesCheckMate(currentPlayer, start, end)) {
            System.out.println("Invalid move. Check mate possibility is there.");
            return false;
        }

        // record move
        movesHistory.add(new Move(currentPlayer, start, end));

        // execute the move
        Piece pieceCaptured = end.getPiece(); // player class can store the captured piece
        end.setPiece(pieceToMove);
        start.setPiece(null);

        switchPlayer();
        updateGameStatus();

        return true;
    }

    private void updateGameStatus() {
        if (isChekMate(currentPlayer)) {
            status = GameStatus.CHECK_MATE;
        } else if (isStaleMate(currentPlayer)) {
            status = GameStatus.STALE_MATE;
        }
    }

    private boolean isChekMate(Player player) {
        // king is checked and no legal moves are possible for the king
        return isKingInCheck(player) && !hasLegalMoves(player);
    }

    private boolean hasLegalMoves(Player player) {
        // find king position of player
        // check if king have any legal moves to make
        return true;
    }

    private boolean isKingInCheck(Player player) {
        // find king position of player
        // check if any oponent piece is attacking the king
        return false;
    }

    private boolean isStaleMate(Player player) {
        // king is not checked and no legal moves are possible for the king
        return !isKingInCheck(player) && !hasLegalMoves(player);
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

    private boolean causesCheckMate(Player currentPlayer, Cell start, Cell end) {
        // make the move
        // check if the currentPlayer is having check mate condition satisfied after the move
        // if yes ... then return true , otherwise return false
        // make sure to return the pieces to the old position again
        return false;
    }




}
