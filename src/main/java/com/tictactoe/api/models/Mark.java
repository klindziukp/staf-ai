package com.tictactoe.api.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a mark on the Tic Tac Toe board.
 * Can be empty (.), X, or O.
 */
@Getter
@RequiredArgsConstructor
public enum Mark {
    EMPTY("."),
    X("X"),
    O("O");

    @JsonValue
    private final String value;

    /**
     * Parse mark from string value.
     *
     * @param value string representation of mark
     * @return corresponding Mark enum
     * @throws IllegalArgumentException if value is invalid
     */
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
