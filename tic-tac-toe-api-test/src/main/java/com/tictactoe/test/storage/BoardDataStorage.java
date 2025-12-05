/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.storage;

import com.tictactoe.test.model.BoardStatus;
import com.tictactoe.test.model.Mark;

/**
 * Storage class for test data related to board states.
 */
public final class BoardDataStorage {

    private BoardDataStorage() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Returns an empty board state.
     *
     * @return BoardStatus with empty board
     */
    public static BoardStatus emptyBoard() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Mark.EMPTY.getValue();
            }
        }
        return BoardStatus.builder()
                .winner(Mark.EMPTY.getValue())
                .board(board)
                .build();
    }

    /**
     * Returns a board state with X winning horizontally in the first row.
     *
     * @return BoardStatus with X as winner
     */
    public static BoardStatus xWinsHorizontal() {
        String[][] board = {
                {"X", "X", "X"},
                {"O", "O", "."},
                {".", ".", "."}
        };
        return BoardStatus.builder()
                .winner(Mark.X.getValue())
                .board(board)
                .build();
    }

    /**
     * Returns a board state with O winning vertically in the first column.
     *
     * @return BoardStatus with O as winner
     */
    public static BoardStatus oWinsVertical() {
        String[][] board = {
                {"O", "X", "X"},
                {"O", "X", "."},
                {"O", ".", "."}
        };
        return BoardStatus.builder()
                .winner(Mark.O.getValue())
                .board(board)
                .build();
    }

    /**
     * Returns a board state with X winning diagonally.
     *
     * @return BoardStatus with X as winner
     */
    public static BoardStatus xWinsDiagonal() {
        String[][] board = {
                {"X", "O", "O"},
                {"O", "X", "."},
                {".", ".", "X"}
        };
        return BoardStatus.builder()
                .winner(Mark.X.getValue())
                .board(board)
                .build();
    }

    /**
     * Returns a board state representing a draw.
     *
     * @return BoardStatus with no winner
     */
    public static BoardStatus drawBoard() {
        String[][] board = {
                {"X", "O", "X"},
                {"O", "X", "O"},
                {"O", "X", "O"}
        };
        return BoardStatus.builder()
                .winner(Mark.EMPTY.getValue())
                .board(board)
                .build();
    }
}
