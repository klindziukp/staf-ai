package com.uspto.api.services;

import com.uspto.api.client.RestClient;
import com.uspto.api.constants.ApiEndpoints;
import com.uspto.api.models.DataSetListResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for Dataset-related API operations.
 * Provides methods to interact with dataset endpoints.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public class DataSetService {

    private final RestClient restClient;

    /**
     * Constructor initializes the service with REST client.
     */
    public DataSetService() {
        this.restClient = new RestClient();
    }

    /**
     * Lists all available datasets.
     *
     * @return Response containing list of datasets
     */
    @Step("Get list of all available datasets")
    public Response listDataSets() {
        log.info("Fetching list of all available datasets");
        Response response = restClient.get(ApiEndpoints.LIST_DATASETS);
        log.info("Retrieved {} datasets", response.jsonPath().getInt("total"));
        return response;
    }

    /**
     * Lists searchable fields for a specific dataset and version.
     *
     * @param dataset dataset name
     * @param version version number
     * @return Response containing list of searchable fields
     */
    @Step("Get searchable fields for dataset: {dataset}, version: {version}")
    public Response listFields(String dataset, String version) {
        log.info("Fetching searchable fields for dataset: {}, version: {}", dataset, version);
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("dataset", dataset);
        pathParams.put("version", version);
        
        Response response = restClient.get(ApiEndpoints.LIST_FIELDS, pathParams);
        log.info("Retrieved fields for dataset: {}", dataset);
        return response;
    }

    /**
     * Parses the dataset list response into POJO.
     *
     * @param response API response
     * @return DataSetListResponse object
     */
    public DataSetListResponse parseDataSetListResponse(Response response) {
        return response.as(DataSetListResponse.class);
    }

    /**
     * Gets the total count of available datasets.
     *
     * @param response API response
     * @return total count of datasets
     */
    public int getTotalDataSetsCount(Response response) {
        return response.jsonPath().getInt("total");
    }

    /**
     * Gets API key for a specific dataset by index.
     *
     * @param response API response
     * @param index dataset index
     * @return API key
     */
    public String getApiKeyByIndex(Response response, int index) {
        return response.jsonPath().getString("apis[" + index + "].apiKey");
    }

    /**
     * Gets API version for a specific dataset by index.
     *
     * @param response API response
     * @param index dataset index
     * @return API version
     */
    public String getApiVersionByIndex(Response response, int index) {
        return response.jsonPath().getString("apis[" + index + "].apiVersionNumber");
    }

    /**
     * Checks if a specific dataset exists in the response.
     *
     * @param response API response
     * @param datasetName dataset name to check
     * @return true if dataset exists, false otherwise
     */
    public boolean isDataSetExists(Response response, String datasetName) {
        DataSetListResponse dataSetList = parseDataSetListResponse(response);
        return dataSetList.getApis().stream()
                .anyMatch(api -> api.getApiKey().equals(datasetName));
    }
}
