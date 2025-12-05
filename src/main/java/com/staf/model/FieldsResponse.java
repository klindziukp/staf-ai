package com.staf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Model representing the fields response for a dataset.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldsResponse {
    
    @JsonProperty("dataset")
    private String dataset;
    
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("fields")
    private List<String> fields;
}
