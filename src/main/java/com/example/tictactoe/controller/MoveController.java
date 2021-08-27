package com.example.tictactoe.controller;

import com.example.tictactoe.bean.Game;
import com.example.tictactoe.bean.Square;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MoveController {

    private Game game = new Game();

    @GetMapping("/move/{position}")
    public ResponseEntity<Game> move(@PathVariable("position") int position) {

        if (position < 0 || position > 8) {
            String message = "Position is not valid";
            log.info(message);
            throw new RuntimeException(message);
        }

        if (findByPosition(position) != null) {
            String message = "Position is not empty";
            log.info(message);
            throw new RuntimeException(message);
        }

        Square square = new Square(position, game.isXIsNext() ? "X" : "O");
        game.getSquares().add(square);
        game.setXIsNext(!game.isXIsNext());
        game.setStepNumber(1 + game.getStepNumber());

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private Square findByPosition(int position) {
        for (Square square : game.getSquares()) {
            if (square.getPosition() == position) {
                return square;
            }
        }
        return null;
    }
}
