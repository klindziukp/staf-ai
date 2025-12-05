/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing the complete board status including winner and board state.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardStatus {
    private String winner;
    private String[][] board;
}
