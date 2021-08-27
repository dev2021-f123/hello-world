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

    private boolean gameOver = false;

    @GetMapping("/restart")
    public ResponseEntity<Game> restart() {
        game = new Game();
        gameOver = false;
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

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

        if (gameOver) {
            String message = "Game is over";
            log.info(message);
            throw new RuntimeException(message);
        }

        Square square = new Square(position, game.isXIsNext() ? "X" : "O");
        game.getSquares().add(square);
        game.setXIsNext(!game.isXIsNext());
        game.setStepNumber(1 + game.getStepNumber());

        String winner = findWinner(game);
        if (winner != null) {
            gameOver = true;
            log.info("Winner is {}", winner);
            return new ResponseEntity<>(game, HttpStatus.ACCEPTED);
        } else if (game.getSquares().size() == 9) {
            String message = "Game is draw";
            log.info(message);
            throw new RuntimeException(message);
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    private String findWinner(Game game) {
        // winning lines of same content
        int[][] lines = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };

        for (int i = 0; i < lines.length; i++) {
            int[] line = lines[i];

            int count = countNonNullOnLine(line);
            if (count == 3) {
                Square squareA = findByPosition(line[0]);
                Square squareB = findByPosition(line[1]);
                Square squareC = findByPosition(line[2]);
                if (squareA.getContent() != null &&
                        squareA.getContent().equals(squareB.getContent()) &&
                        squareA.getContent().equals(squareC.getContent())) {
                    log.info("Winning line is {} - {} - {}", line[0], line[1], line[2]);
                    return squareA.getContent();
                }
            }
        }
        return null;
    }

    private int countNonNullOnLine(int[] line) {
        int count = 0;
        for (int j = 0; j < line.length; j++) {
            Square square = findByPosition(line[j]);
            if (square != null) {
                count++;
            }
        }
        return count;
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
