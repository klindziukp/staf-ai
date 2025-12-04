package com.klindziuk.petstore.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error model representing an error response from the Petstore API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    
    @SerializedName("code")
    private Integer code;
    
    @SerializedName("message")
    private String message;
}
