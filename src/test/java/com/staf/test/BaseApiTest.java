package com.staf.test;

import com.staf.config.ApiConfig;
import com.staf.constant.ApiConstants;
import com.staf.listener.TestMethodCaptureListener;
import com.staf.listener.TestSuiteListener;
import com.staf.service.ResponseVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

/**
 * Base test class for all API tests.
 * Provides common setup and utility methods.
 */
@Slf4j
@Listeners({TestSuiteListener.class, TestMethodCaptureListener.class})
public abstract class BaseApiTest {

    protected ApiConfig apiConfig;
    protected ResponseVerificationService verificationService;
    protected String baseUrl;
    protected String defaultDataset;
    protected String defaultVersion;

    @BeforeClass
    public void baseSetUp() {
        log.info("Initializing base test setup");
        
        // Initialize configuration
        apiConfig = ApiConfig.getInstance();
        baseUrl = apiConfig.getFullBaseUrl();
        defaultDataset = apiConfig.getDefaultDataset();
        defaultVersion = apiConfig.getDefaultVersion();
        
        // Initialize services
        verificationService = new ResponseVerificationService();
        
        log.info("Base URL: {}", baseUrl);
        log.info("Default Dataset: {}", defaultDataset);
        log.info("Default Version: {}", defaultVersion);
        log.info("Base test setup completed");
    }

    /**
     * Build path with dataset and version parameters.
     *
     * @param pathTemplate path template with placeholders
     * @param dataset      dataset name
     * @param version      version
     * @return formatted path
     */
    protected String buildPath(String pathTemplate, String dataset, String version) {
        return pathTemplate
                .replace("{dataset}", dataset)
                .replace("{version}", version);
    }

    /**
     * Build path with default dataset and version.
     *
     * @param pathTemplate path template with placeholders
     * @return formatted path
     */
    protected String buildPath(String pathTemplate) {
        return buildPath(pathTemplate, defaultDataset, defaultVersion);
    }

    /**
     * Log test step.
     *
     * @param stepDescription step description
     */
    protected void logTestStep(String stepDescription) {
        log.info("TEST STEP: {}", stepDescription);
    }

    /**
     * Log request details.
     *
     * @param method HTTP method
     * @param url    request URL
     */
    protected void logRequest(String method, String url) {
        log.info("→ {} {}", method, url);
    }

    /**
     * Log response details.
     *
     * @param statusCode   HTTP status code
     * @param responseBody response body
     */
    protected void logResponse(int statusCode, String responseBody) {
        log.info("← Status: {}", statusCode);
        log.debug("Response body: {}", responseBody);
    }

    /**
     * Verify successful response.
     *
     * @param statusCode   actual status code
     * @param responseBody response body
     */
    protected void verifySuccessfulResponse(int statusCode, String responseBody) {
        verificationService.verifyStatusCode(statusCode, ApiConstants.STATUS_OK);
        verificationService.verifyResponseNotEmpty(responseBody);
        verificationService.verifyValidJson(responseBody);
    }

    /**
     * Verify not found response.
     *
     * @param statusCode actual status code
     */
    protected void verifyNotFoundResponse(int statusCode) {
        verificationService.verifyStatusCode(statusCode, ApiConstants.STATUS_NOT_FOUND);
    }
}
