package com.example.tictactoe.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {
    List<Square> squares;
    int stepNumber;
    boolean xIsNext;

    public Game() {
        squares = new ArrayList();
        stepNumber = 0;
        xIsNext = true;
    }
}
