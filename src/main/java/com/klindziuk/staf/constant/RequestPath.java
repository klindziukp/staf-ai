package com.klindziuk.staf.constant;

import lombok.experimental.UtilityClass;

/**
 * Constants for USPTO Data Set API request paths.
 */
@UtilityClass
public class RequestPath {

    /**
     * Base URL for USPTO Data Set API.
     */
    public static final String BASE_URL = "https://developer.uspto.gov/ds-api";

    /**
     * Root endpoint to list all available data sets.
     */
    public static final String ROOT = "/";

    /**
     * Endpoint to get searchable fields for a specific dataset.
     * Usage: String.format(FIELDS, dataset, version)
     */
    public static final String FIELDS = "/%s/%s/fields";

    /**
     * Endpoint to search records in a specific dataset.
     * Usage: String.format(RECORDS, dataset, version)
     */
    public static final String RECORDS = "/%s/%s/records";

    /**
     * Default dataset name.
     */
    public static final String DEFAULT_DATASET = "oa_citations";

    /**
     * Default API version.
     */
    public static final String DEFAULT_VERSION = "v1";

    /**
     * Alternative dataset name.
     */
    public static final String CANCER_MOONSHOT_DATASET = "cancer_moonshot";
}
