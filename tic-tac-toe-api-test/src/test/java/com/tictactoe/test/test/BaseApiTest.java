/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.test;

import com.tictactoe.test.config.ServiceConfig;
import com.tictactoe.test.listener.TestMethodCaptureListener;
import com.tictactoe.test.listener.TestSuiteListener;
import com.tictactoe.test.service.ResponseVerificationService;
import com.staf.api.core.IApiClient;
import com.staf.api.core.serialization.ISerializationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.HashMap;
import java.util.Map;

/**
 * Base test class for all API tests.
 * Provides common setup, teardown, and utility methods.
 */
@Slf4j
@Listeners({TestSuiteListener.class, TestMethodCaptureListener.class})
public abstract class BaseApiTest {

    protected final ResponseVerificationService rvs = new ResponseVerificationService();
    protected ISerializationStrategy serializationStrategy;
    protected IApiClient iApiClient;

    @BeforeSuite
    public void beforeSuite() {
        log.info("Initializing test suite with base URL: {}", ServiceConfig.BASE_URL);
    }

    @AfterSuite
    public void afterSuite() {
        log.info("Test suite execution completed");
    }

    /**
     * Creates default headers for API requests.
     *
     * @return Map of default headers
     */
    protected Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        if (!ServiceConfig.API_KEY.isEmpty()) {
            headers.put("api-key", ServiceConfig.API_KEY);
        }
        return headers;
    }

    /**
     * Creates headers with Bearer token authentication.
     *
     * @return Map of headers with Bearer token
     */
    protected Map<String, String> getBearerAuthHeaders() {
        Map<String, String> headers = getDefaultHeaders();
        if (!ServiceConfig.BEARER_TOKEN.isEmpty()) {
            headers.put("Authorization", "Bearer " + ServiceConfig.BEARER_TOKEN);
        }
        return headers;
    }

    /**
     * Validates coordinate values for board positions.
     *
     * @param row row coordinate (1-3)
     * @param column column coordinate (1-3)
     * @return true if coordinates are valid
     */
    protected boolean isValidCoordinate(int row, int column) {
        return row >= 1 && row <= 3 && column >= 1 && column <= 3;
    }
}
