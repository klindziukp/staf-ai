package com.tictactoe.api.utils;

import com.tictactoe.api.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Assured configuration utility.
 * Provides pre-configured request and response specifications.
 */
@Slf4j
public final class RestAssuredConfig {
    private static final ApiConfig API_CONFIG = ApiConfig.getInstance();

    private RestAssuredConfig() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Initializes REST Assured with default configuration.
     */
    public static void init() {
        RestAssured.baseURI = API_CONFIG.getBaseUrl();
        RestAssured.config = getRestAssuredConfig();
        log.info("REST Assured initialized with base URI: {}", RestAssured.baseURI);
    }

    /**
     * Gets default request specification.
     *
     * @return configured RequestSpecification
     */
    public static RequestSpecification getDefaultRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured());

        if (API_CONFIG.isEnableRequestLogging()) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }

    /**
     * Gets default response specification.
     *
     * @return configured ResponseSpecification
     */
    public static ResponseSpecification getDefaultResponseSpec() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON);

        if (API_CONFIG.isEnableResponseLogging()) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }

    /**
     * Gets REST Assured configuration.
     *
     * @return configured RestAssuredConfig
     */
    private static io.restassured.config.RestAssuredConfig getRestAssuredConfig() {
        return io.restassured.config.RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .defaultObjectMapperType(ObjectMapperType.JACKSON_2))
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", API_CONFIG.getConnectionTimeout())
                        .setParam("http.socket.timeout", API_CONFIG.getSocketTimeout()))
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails()
                        .enablePrettyPrinting(true));
    }

    /**
     * Resets REST Assured to default state.
     */
    public static void reset() {
        RestAssured.reset();
        log.debug("REST Assured configuration reset");
    }
}
