package com.tictactoe.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
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
     * Creates a Mark from its string representation.
     *
     * @param value the string value
     * @return the corresponding Mark
     * @throws IllegalArgumentException if the value is invalid
     */
    @JsonCreator
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
