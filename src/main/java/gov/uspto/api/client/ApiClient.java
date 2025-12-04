package gov.uspto.api.client;

import gov.uspto.api.config.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base API client for REST Assured configuration
 */
public class ApiClient {
    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private static final Configuration config = Configuration.getInstance();
    private static RequestSpecification requestSpec;

    static {
        initializeRestAssured();
    }

    private static void initializeRestAssured() {
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        RestAssuredConfig restAssuredConfig = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.getConnectionTimeout())
                        .setParam("http.socket.timeout", config.getReadTimeout()));
        
        RestAssured.config = restAssuredConfig;
        
        log.info("REST Assured initialized with base URL: {}", config.getBaseUrl());
    }

    public static RequestSpecification getRequestSpecification() {
        if (requestSpec == null) {
            requestSpec = new RequestSpecBuilder()
                    .setBaseUri(config.getBaseUrl())
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .addFilter(new AllureRestAssured())
                    .log(config.isLoggingEnabled() ? LogDetail.ALL : LogDetail.NONE)
                    .build();
        }
        return requestSpec;
    }

    public static RequestSpecification createNewRequestSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getRequestSpecification())
                .build();
    }
}
