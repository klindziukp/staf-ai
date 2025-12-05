package com.staf.uspto.constant;

import lombok.experimental.UtilityClass;

/**
 * Utility class containing API endpoint paths for USPTO Data Set API.
 */
@UtilityClass
public class RequestPath {
    
    /**
     * Base URL for USPTO Data Set API.
     */
    public static final String BASE_URL = "https://developer.uspto.gov/ds-api";
    
    /**
     * Endpoint to list all available datasets.
     */
    public static final String LIST_DATASETS = "/";
    
    /**
     * Endpoint template to get searchable fields for a dataset.
     * Usage: String.format(GET_FIELDS, dataset, version)
     */
    public static final String GET_FIELDS = "/%s/%s/fields";
    
    /**
     * Endpoint template to search records in a dataset.
     * Usage: String.format(SEARCH_RECORDS, dataset, version)
     */
    public static final String SEARCH_RECORDS = "/%s/%s/records";
    
    /**
     * Default dataset name for testing.
     */
    public static final String DEFAULT_DATASET = "oa_citations";
    
    /**
     * Default version for testing.
     */
    public static final String DEFAULT_VERSION = "v1";
}
