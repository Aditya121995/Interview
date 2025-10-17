package com.problems.snakeAndLadder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Game {
    private final Board board;
    private final Deque<Player> players;
    private final Dice dice;
    private GameStatus status;
    private Player winner;
    private final int bonusNumber;

    public Game(int startOfBoard, int endOfBoard, List<String> playerName, Dice dice,
                List<BoardElement> boardElements, int bonusNumber) {
        this.players = new ArrayDeque<>();
        this.board = new Board(startOfBoard, endOfBoard, boardElements);
        this.dice = dice;
        this.status = GameStatus.NOT_STARTED;
        this.winner = null;
        this.bonusNumber = bonusNumber;

        if (playerName.isEmpty() || playerName.size() < 2) {
            throw new IllegalArgumentException("Player list cannot be empty or less than 2");
        }

        for (String name : playerName) {
            players.add(new Player(name, startOfBoard));
        }
    }

    public void play() {
        this.status = GameStatus.RUNNING;
        System.out.println("Game Started");

        while (status == GameStatus.RUNNING) {
            Player currentPlayer = players.poll();
            takeTurn(currentPlayer);

            if (status == GameStatus.RUNNING) {
                players.addLast(currentPlayer);
            }
        }

        System.out.println("Game Finished");
        if (winner != null) {
            System.out.println("Winner is " + winner.getPlayerName());
        }
    }

    public void takeTurn(Player currentPlayer) {
        int roll = dice.roll();
        System.out.println(String.format("%s turn. rolled dice %d",  currentPlayer.getPlayerName(), roll));

        int currentPosition = currentPlayer.getPosition();
        int nextPosition = currentPosition + roll;

        if (nextPosition > board.getEndOfBoard()) {
            System.out.println(String.format("%s have to skip the turn.", currentPlayer.getPlayerName()));
            return;
        }

        if (nextPosition == board.getEndOfBoard()) {
            this.status = GameStatus.FINISHED;
            this.winner = currentPlayer;
            currentPlayer.setPosition(nextPosition);
            System.out.println(String.format("%s reached the final square." ,winner.getPlayerName()));
            return;
        }

        int finalPosition = board.getFinalPosition(nextPosition);

        if (finalPosition > nextPosition) {
            System.out.println(String.format("%s found ladder at %d and climbed to position %d",
                    currentPlayer.getPlayerName(), nextPosition, finalPosition));
        } else if (finalPosition < nextPosition) {
            System.out.println(String.format("%s found snake at %d and fall down to position %d",
                    currentPlayer.getPlayerName(), nextPosition, finalPosition));
        } else  {
            System.out.println(String.format("%s moved to the position %d.",
                    currentPlayer.getPlayerName(),  finalPosition));
        }

        currentPlayer.setPosition(finalPosition);

        if (roll == bonusNumber) {
            System.out.println(String.format("%s got the bonus number %d and got another turn.",
                    currentPlayer.getPlayerName(),  bonusNumber));
            takeTurn(currentPlayer);
        }
    }
}
