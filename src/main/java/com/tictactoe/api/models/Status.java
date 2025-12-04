package com.tictactoe.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the game status including winner and current board state.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    @JsonProperty("winner")
    private Mark winner;

    @JsonProperty("board")
    private Mark[][] board;

    /**
     * Checks if the game has a winner.
     *
     * @return true if there is a winner
     */
    public boolean hasWinner() {
        return winner != null && winner != Mark.EMPTY;
    }

    /**
     * Checks if the game is a draw.
     *
     * @return true if the board is full and there's no winner
     */
    public boolean isDraw() {
        if (hasWinner() || board == null) {
            return false;
        }
        for (Mark[] row : board) {
            for (Mark mark : row) {
                if (mark == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the game is still in progress.
     *
     * @return true if the game is not finished
     */
    public boolean isInProgress() {
        return !hasWinner() && !isDraw();
    }

    /**
     * Gets the mark at the specified position.
     *
     * @param row    the row (1-3)
     * @param column the column (1-3)
     * @return the mark at the position
     */
    public Mark getMark(int row, int column) {
        if (board == null || row < 1 || row > 3 || column < 1 || column > 3) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        return board[row - 1][column - 1];
    }
}
