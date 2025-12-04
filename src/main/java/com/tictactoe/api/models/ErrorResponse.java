package com.tictactoe.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an error response from the API.
 * Contains error message with max length of 256 characters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String error;
    private Integer status;

    /**
     * Gets the primary error message.
     *
     * @return error message
     */
    public String getErrorMessage() {
        return message != null ? message : error;
    }

    /**
     * Validates that error message doesn't exceed max length.
     *
     * @return true if message length is valid
     */
    public boolean isMessageLengthValid() {
        String msg = getErrorMessage();
        return msg != null && msg.length() <= 256;
    }
}
