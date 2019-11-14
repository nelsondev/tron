package com.nelsontron.siege.game;

import com.nelsontron.siege.entity.Fighter;

import java.util.List;

public interface Game {

    // getters
    List<Fighter> getBlue();
    List<Fighter> getRed();
    String getState();
    int getTime();

    // setters
    void setTime(int time);

    // methods
    void start(int seconds);
    void stop();
    void reset();
}
