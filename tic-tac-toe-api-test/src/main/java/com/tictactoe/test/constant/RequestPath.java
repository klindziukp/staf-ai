/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.constant;

/**
 * Constants for API request paths.
 */
public final class RequestPath {

    public static final String GET_BOARD = "/board";
    public static final String GET_SQUARE = "/board/%s/%s";
    public static final String PUT_SQUARE = "/board/%s/%s";

    private RequestPath() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
