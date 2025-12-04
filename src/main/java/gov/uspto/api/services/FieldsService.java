package gov.uspto.api.services;

import gov.uspto.api.client.RequestSpecificationBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Service class for fields-related API operations
 */
public class FieldsService {
    private static final Logger log = LoggerFactory.getLogger(FieldsService.class);
    private static final String FIELDS_ENDPOINT = "/{dataset}/{version}/fields";

    @Step("Get searchable fields for dataset: {dataset}, version: {version}")
    public Response getFields(String dataset, String version) {
        log.info("Fetching searchable fields for dataset: {}, version: {}", dataset, version);
        
        return given()
                .spec(new RequestSpecificationBuilder()
                        .addPathParam("dataset", dataset)
                        .addPathParam("version", version)
                        .build())
                .when()
                .get(FIELDS_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
