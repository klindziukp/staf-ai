package com.klindziuk.staf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing API information for a dataset.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {

    @JsonProperty("apiKey")
    private String apiKey;

    @JsonProperty("apiVersionNumber")
    private String apiVersionNumber;

    @JsonProperty("apiUrl")
    private String apiUrl;

    @JsonProperty("apiDocumentationUrl")
    private String apiDocumentationUrl;
}
