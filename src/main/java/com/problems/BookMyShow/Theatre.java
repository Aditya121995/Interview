package com.problems.BookMyShow;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Theatre {
    private final String id;
    private final String name;
    private final City city;
    private List<Screen> screens;

    public Theatre(String id, String name, City city) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen s){
        this.screens.add(s);
    }
}
