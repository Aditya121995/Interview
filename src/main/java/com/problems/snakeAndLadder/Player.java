package com.problems.snakeAndLadder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Player {
    private final String playerName;
    @Setter
    private int position;

    public Player(String playerName, int position) {
        this.playerName = playerName;
        this.position = position;
    }
}
