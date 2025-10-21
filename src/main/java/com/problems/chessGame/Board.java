package com.problems.chessGame;

import lombok.Getter;

@Getter
public class Board {
    private static Board instance;
    private final Cell[][] board;

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    private Board() {
        this.board = new Cell[8][8];
        initializeCells();
    }

    private void initializeCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Cell(i, j);
            }
        }

        // set pawns
        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(new Pawn(Color.WHITE));
            board[6][i].setPiece(new Pawn(Color.BLACK));
        }

        // set other pieces for white
        board[0][0].setPiece(new Rook(Color.WHITE));
        board[0][7].setPiece(new Rook(Color.WHITE));
        board[0][1].setPiece(new Knight(Color.WHITE));
        board[0][6].setPiece(new Knight(Color.WHITE));
        board[0][2].setPiece(new Bishop(Color.WHITE));
        board[0][5].setPiece(new Bishop(Color.WHITE));
        board[0][4].setPiece(new King(Color.WHITE));
        board[0][3].setPiece(new Queen(Color.WHITE));

        // set other pieces for Black
        board[7][0].setPiece(new Rook(Color.BLACK));
        board[7][7].setPiece(new Rook(Color.BLACK));
        board[7][1].setPiece(new Knight(Color.BLACK));
        board[7][6].setPiece(new Knight(Color.BLACK));
        board[7][2].setPiece(new Bishop(Color.BLACK));
        board[7][5].setPiece(new Bishop(Color.BLACK));
        board[7][4].setPiece(new King(Color.BLACK));
        board[7][3].setPiece(new Queen(Color.BLACK));
    }

    public  Cell getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isPathClear(Cell start, Cell end) {
        int rowDir = Integer.compare(end.getRow() - start.getRow(), 0);
        int colDir = Integer.compare(end.getCol() - start.getCol(), 0);

        int endRow = end.getRow();
        int endCol = end.getCol();

        int x = start.getRow() + rowDir;
        int y = start.getCol() + colDir;

        while (x != endRow || y != endCol) {
            if (board[x][y].getPiece() != null) {
                return false;
            }

            if (x != endRow) {
                x += rowDir;
            }

            if (y != endCol) {
                y += colDir;
            }
        }

        return true;
    }
}
