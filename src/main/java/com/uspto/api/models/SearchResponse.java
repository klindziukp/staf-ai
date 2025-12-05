package com.uspto.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * POJO class representing the search response for record searches.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    /**
     * Response header containing metadata about the search.
     */
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader;

    /**
     * Response containing the actual search results.
     */
    @JsonProperty("response")
    private ResponseData response;

    /**
     * Inner class representing response header.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseHeader {

        /**
         * Status code of the search operation.
         */
        @JsonProperty("status")
        private Integer status;

        /**
         * Query time in milliseconds.
         */
        @JsonProperty("QTime")
        private Integer qTime;

        /**
         * Parameters used in the search.
         */
        @JsonProperty("params")
        private Map<String, Object> params;
    }

    /**
     * Inner class representing response data.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseData {

        /**
         * Total number of documents found.
         */
        @JsonProperty("numFound")
        private Integer numFound;

        /**
         * Starting position of the results.
         */
        @JsonProperty("start")
        private Integer start;

        /**
         * Maximum score in the results.
         */
        @JsonProperty("maxScore")
        private Double maxScore;

        /**
         * List of document records.
         */
        @JsonProperty("docs")
        private List<Map<String, Object>> docs;
    }
}
