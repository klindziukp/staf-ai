package com.uspto.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * POJO class representing the response for listing available datasets.
 * Maps to the dataSetList schema in the OpenAPI specification.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSetListResponse {

    /**
     * Total number of available datasets.
     */
    @JsonProperty("total")
    private Integer total;

    /**
     * List of available API datasets.
     */
    @JsonProperty("apis")
    private List<ApiInfo> apis;

    /**
     * Inner class representing individual API information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiInfo {

        /**
         * API key to be used as dataset parameter value.
         */
        @JsonProperty("apiKey")
        private String apiKey;

        /**
         * API version number to be used as version parameter value.
         */
        @JsonProperty("apiVersionNumber")
        private String apiVersionNumber;

        /**
         * URL describing the dataset's fields.
         */
        @JsonProperty("apiUrl")
        private String apiUrl;

        /**
         * URL to the API console for the API.
         */
        @JsonProperty("apiDocumentationUrl")
        private String apiDocumentationUrl;
    }
}
