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

@WebMvcTest(MoveController.class)
class MoveControllerTest {

    @Autowired
    MockMvc mockMvc;

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