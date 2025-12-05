package com.staf.constant;

/**
 * General API constants for USPTO Data Set API.
 */
public final class ApiConstants {

    private ApiConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Default values
    public static final String DEFAULT_DATASET = "oa_citations";
    public static final String DEFAULT_VERSION = "v1";
    public static final String DEFAULT_CRITERIA = "*:*";
    public static final int DEFAULT_START = 0;
    public static final int DEFAULT_ROWS = 100;
    
    // HTTP Headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    
    // Response fields
    public static final String FIELD_TOTAL = "total";
    public static final String FIELD_APIS = "apis";
    public static final String FIELD_API_KEY = "apiKey";
    public static final String FIELD_API_VERSION_NUMBER = "apiVersionNumber";
    public static final String FIELD_API_URL = "apiUrl";
    public static final String FIELD_API_DOCUMENTATION_URL = "apiDocumentationUrl";
    public static final String FIELD_DOCS = "docs";
    public static final String FIELD_NUM_FOUND = "numFound";
    
    // HTTP Status Codes
    public static final int STATUS_OK = 200;
    public static final int STATUS_NOT_FOUND = 404;
}
