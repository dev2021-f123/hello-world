package com.example.tictactoe.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoveController.class)
class MoveControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DirtiesContext
    @Test
    public void gameIsDraw() {
        Exception exception = assertThrows(NestedServletException.class, () -> {

            // no winner after 9 moves, game is draw
            int[] moves = {4, 0, 1, 7, 6, 2, 5, 3, 8};
            performGet(moves);
        });

        String expectedMessage = "Game is draw";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DirtiesContext
    @Test
    public void gameIsOver() {
        Exception exception = assertThrows(NestedServletException.class, () -> {

            // game is over at 7th move
            // the 8th move throws runtime exception
            int[] moves = {0, 1, 2, 3, 4, 5, 6, 7};

            performGet(moves);
        });

        String expectedMessage = "Game is over";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DirtiesContext
    @Test
    public void winningMoves() throws Exception {

        // game is over at 7th move and the winner is X
        int[] moves = {0, 1, 2, 3, 4, 5, 6};

        ResultActions perform = performGet(moves);
        perform.andExpect(status().isAccepted());
        perform.andExpect(jsonPath("$.squares[6].content", is("X")));
    }

    @DirtiesContext
    @Test
    public void positionNotEmpty() {
        Exception exception = assertThrows(NestedServletException.class, () -> {

            // the position 0 is already played
            int[] moves = {0, 1, 0};

            performGet(moves);
        });

        String expectedMessage = "Position is not empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DirtiesContext
    @Test
    public void positionNotValid() {
        Exception exception = assertThrows(NestedServletException.class, () -> {

            // the position 50 is out of [0..8]
            int[] moves = {0, 1, 50};

            performGet(moves);
        });

        String expectedMessage = "Position is not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private ResultActions performGet(int[] moves) throws Exception {
        ResultActions perform = null;
        boolean xIsNext = true;

        for (int i = 0; i < moves.length; i++) {
            int position = moves[i];
            xIsNext = !xIsNext;
            perform = mockMvc.perform(MockMvcRequestBuilders
                    .get("/move/" + position)
                    .contentType(MediaType.APPLICATION_JSON));

            // assertions for each move
            perform
                    .andExpect(jsonPath("$.squares", hasSize(i + 1)))
                    .andExpect(jsonPath("$.xisNext", is(xIsNext)));
        }
        return perform;
    }
}