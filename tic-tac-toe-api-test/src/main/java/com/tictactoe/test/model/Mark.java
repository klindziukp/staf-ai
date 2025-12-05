/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing possible values for a board square.
 * '.' means empty square, 'X' and 'O' are player marks.
 */
public enum Mark {
    EMPTY("."),
    X("X"),
    O("O");

    private final String value;

    Mark(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static Mark fromValue(String value) {
        for (Mark mark : Mark.values()) {
            if (mark.value.equals(value)) {
                return mark;
            }
        }
        throw new IllegalArgumentException("Invalid mark value: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
