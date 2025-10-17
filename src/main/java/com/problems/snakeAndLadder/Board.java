package com.problems.snakeAndLadder;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Board {
    private final int startOfBoard;
    @Getter
    private final int endOfBoard;
    private final Map<Integer, Integer> snakeAndLadder;

    public Board(int start, int end, List<BoardElement> boardElements) {
        this.startOfBoard = start;
        this.endOfBoard = end;
        this.snakeAndLadder = new ConcurrentHashMap<>();

        fillBoardElements(boardElements);


    }

    private void fillBoardElements(List<BoardElement> boardElements) {
        for (BoardElement snl : boardElements) {
            if (BoardElementType.SNAKE.equals(snl.getType())) {
                if (snl.getEnd() == this.endOfBoard) {
                    throw new IllegalArgumentException("Snake head cannot be present at end of board");
                }
            }

            if (BoardElementType.LADDER.equals(snl.getType())) {
                if (snl.getStart() == startOfBoard) {
                    throw new IllegalArgumentException("Ladder head cannot be present at start of board");
                }
            }

            snakeAndLadder.put(snl.getStart(), snl.getEnd());
        }
    }

    public int getFinalPosition(int position) {
        return snakeAndLadder.getOrDefault(position, position);
    }
}
