package com.staf.constant;

/**
 * Constants for USPTO Data Set API request paths.
 */
public final class RequestPath {

    private RequestPath() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Base paths
    public static final String ROOT = "/";
    
    // Metadata endpoints
    public static final String DATASET_FIELDS = "/{dataset}/{version}/fields";
    
    // Search endpoints
    public static final String DATASET_RECORDS = "/{dataset}/{version}/records";
    
    // Path parameters
    public static final String PARAM_DATASET = "dataset";
    public static final String PARAM_VERSION = "version";
    
    // Query/Form parameters
    public static final String PARAM_CRITERIA = "criteria";
    public static final String PARAM_START = "start";
    public static final String PARAM_ROWS = "rows";
}
