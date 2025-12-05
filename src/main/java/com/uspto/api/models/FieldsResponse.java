package com.uspto.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * POJO class representing the response for listing searchable fields.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldsResponse {

    /**
     * Dataset name.
     */
    @JsonProperty("dataset")
    private String dataset;

    /**
     * Version of the dataset.
     */
    @JsonProperty("version")
    private String version;

    /**
     * List of searchable field names.
     */
    @JsonProperty("fields")
    private List<String> fields;

    /**
     * Description of the dataset.
     */
    @JsonProperty("description")
    private String description;

    /**
     * Total number of fields.
     */
    @JsonProperty("totalFields")
    private Integer totalFields;
}
