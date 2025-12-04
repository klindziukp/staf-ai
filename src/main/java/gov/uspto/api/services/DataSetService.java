package gov.uspto.api.services;

import gov.uspto.api.client.ApiClient;
import gov.uspto.api.models.response.DataSetListResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Service class for dataset-related API operations
 */
public class DataSetService {
    private static final Logger log = LoggerFactory.getLogger(DataSetService.class);
    private static final String DATASETS_ENDPOINT = "/";

    @Step("Get list of available datasets")
    public Response getDataSets() {
        log.info("Fetching list of available datasets");
        
        return given()
                .spec(ApiClient.getRequestSpecification())
                .when()
                .get(DATASETS_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Get list of available datasets and parse response")
    public DataSetListResponse getDataSetsAsPojo() {
        Response response = getDataSets();
        return response.as(DataSetListResponse.class);
    }

    @Step("Verify dataset exists: {datasetName}")
    public boolean datasetExists(String datasetName) {
        DataSetListResponse response = getDataSetsAsPojo();
        return response.getApis().stream()
                .anyMatch(ds -> ds.getApiKey().equalsIgnoreCase(datasetName));
    }
}
