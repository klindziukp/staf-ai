package com.uspto.api.constants;

/**
 * Constants class containing all USPTO Data Set API endpoints.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
public final class ApiEndpoints {

    /**
     * Base endpoint to list all available datasets.
     */
    public static final String LIST_DATASETS = "/";

    /**
     * Endpoint to list searchable fields for a specific dataset and version.
     * Parameters: {dataset}, {version}
     */
    public static final String LIST_FIELDS = "/{dataset}/{version}/fields";

    /**
     * Endpoint to search records with criteria for a specific dataset and version.
     * Parameters: {dataset}, {version}
     */
    public static final String SEARCH_RECORDS = "/{dataset}/{version}/records";

    /**
     * Private constructor to prevent instantiation.
     */
    private ApiEndpoints() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }

    /**
     * Builds the list fields endpoint with dataset and version.
     *
     * @param dataset dataset name
     * @param version version number
     * @return formatted endpoint path
     */
    public static String getListFieldsEndpoint(String dataset, String version) {
        return LIST_FIELDS
                .replace("{dataset}", dataset)
                .replace("{version}", version);
    }

    /**
     * Builds the search records endpoint with dataset and version.
     *
     * @param dataset dataset name
     * @param version version number
     * @return formatted endpoint path
     */
    public static String getSearchRecordsEndpoint(String dataset, String version) {
        return SEARCH_RECORDS
                .replace("{dataset}", dataset)
                .replace("{version}", version);
    }
}
