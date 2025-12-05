/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for placing a mark on the board.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkRequest {
    private String mark;
}
