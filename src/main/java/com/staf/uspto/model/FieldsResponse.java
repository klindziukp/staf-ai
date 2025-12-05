package com.staf.uspto.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model class representing fields metadata response from USPTO API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldsResponse {
    
    @SerializedName("dataset")
    private String dataset;
    
    @SerializedName("version")
    private String version;
    
    @SerializedName("fields")
    private List<String> fields;
}
