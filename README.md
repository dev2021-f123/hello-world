# TicTacToe

## Rules

- The rules are described below :

- X always goes first.
- Players cannot play on a played position.
- Players alternate placing X’s and O’s on the board until either:
  - One player has three in a row, horizontally, vertically or diagonally
  - All nine squares are filled.
- If a player is able to draw three X’s or three O’s in a row, that player wins.
- If all nine squares are filled and neither player has three in a row, the game is a draw.

## Compile and installation

    mvn clean install

## Run

    mvn spring-boot:run

## Play

    Each user send a rest request to http://localhost:8080/move/{position}
    where {position} is between 0 and 8 included as in the schema below:

    | 0 | 1 | 2 |
    | 3 | 4 | 5 |
    | 6 | 7 | 8 |

## Status and exceptions

    - when (position < 0) or (position > 8): runtimeException("Position is not valid")
    - when position is not empty: runtimeException("Position is not empty")
    - when there is a winner: the status of the response is HttpStatus.ACCEPTED
    - when the move is good: the status of the response is HttpStatus.OK
    - when game is over and still received request: runtimeException("Game is over")
