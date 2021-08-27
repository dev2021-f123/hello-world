package com.example.tictactoe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Square {

    int position;
    String content;
}
