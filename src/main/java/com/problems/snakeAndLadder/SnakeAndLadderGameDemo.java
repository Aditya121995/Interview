package com.problems.snakeAndLadder;

import java.util.ArrayList;
import java.util.List;

public class SnakeAndLadderGameDemo {
    public static void main(String[] args) {

        List<BoardElement> boardElemets =  new ArrayList<>();
        boardElemets.add(new Snake(70, 20));
        boardElemets.add(new Snake(25, 5));
        boardElemets.add(new Snake(38, 28));
        boardElemets.add(new Snake(53, 45));
        boardElemets.add(new Snake(94, 35));
        boardElemets.add(new Snake(90, 60));
        boardElemets.add(new Ladder(10, 50));
        boardElemets.add(new Ladder(15, 25));
        boardElemets.add(new Ladder(4, 40));
        boardElemets.add(new Ladder(85, 95));
        boardElemets.add(new Ladder(29, 58));

        Game game = new Game(1, 100,
                List.of("Aditya", "Moksha"), new Dice(1, 1, 6),
                boardElemets, 6);
        game.play();
    }
}
