package com.uspto.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO class representing the search request body for searching records.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    /**
     * Search criteria using Lucene Query Syntax.
     * Format: propertyName:value, propertyName:[num1 TO num2]
     * Date range format: propertyName:[yyyyMMdd TO yyyyMMdd]
     */
    @JsonProperty("criteria")
    @Builder.Default
    private String criteria = "*:*";

    /**
     * Starting record number. Default value is 0.
     */
    @JsonProperty("start")
    @Builder.Default
    private Integer start = 0;

    /**
     * Number of rows to be returned. Default value is 100.
     */
    @JsonProperty("rows")
    @Builder.Default
    private Integer rows = 100;
}
