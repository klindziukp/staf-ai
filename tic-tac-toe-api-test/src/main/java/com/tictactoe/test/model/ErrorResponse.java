/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing error response from the API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
