package com.tictactoe.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents the game status response.
 * Contains winner information and current board state.
 */
@Data
@Builder
@Jacksonized
public class Status {
    @JsonProperty("winner")
    private String winner;

    @JsonProperty("board")
    private List<List<String>> board;

    /**
     * Checks if the game has a winner.
     *
     * @return true if winner is X or O
     */
    public boolean hasWinner() {
        return winner != null && (winner.equals("X") || winner.equals("O"));
    }

    /**
     * Checks if the game is still in progress.
     *
     * @return true if winner is "."
     */
    public boolean isInProgress() {
        return ".".equals(winner);
    }

    /**
     * Gets the mark at specified position.
     *
     * @param row    row number (1-3)
     * @param column column number (1-3)
     * @return mark at the position
     */
    public String getMark(int row, int column) {
        if (row < 1 || row > 3 || column < 1 || column > 3) {
            throw new IllegalArgumentException("Coordinates must be between 1 and 3");
        }
        return board.get(row - 1).get(column - 1);
    }
}
