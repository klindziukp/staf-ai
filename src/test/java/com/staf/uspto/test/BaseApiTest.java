package com.staf.uspto.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.staf.uspto.constant.RequestPath;
import com.staf.uspto.listener.TestMethodCaptureListener;
import com.staf.uspto.listener.TestSuiteListener;
import com.staf.uspto.service.ResponseVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

/**
 * Base test class for USPTO API tests.
 * Provides common setup and utility methods for all test classes.
 */
@Slf4j
@Listeners({TestSuiteListener.class, TestMethodCaptureListener.class})
public abstract class BaseApiTest {
    
    protected static final String BASE_URL = RequestPath.BASE_URL;
    protected static final String DEFAULT_DATASET = RequestPath.DEFAULT_DATASET;
    protected static final String DEFAULT_VERSION = RequestPath.DEFAULT_VERSION;
    
    protected Gson gson;
    protected ResponseVerificationService verificationService;
    
    @BeforeClass
    public void baseSetUp() {
        log.info("Setting up Base API Test");
        log.info("Base URL: {}", BASE_URL);
        
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        
        verificationService = new ResponseVerificationService();
        
        log.info("Base setup completed successfully");
    }
    
    /**
     * Builds the full URL for a given endpoint path.
     *
     * @param path the endpoint path
     * @return the full URL
     */
    protected String buildUrl(String path) {
        return BASE_URL + path;
    }
    
    /**
     * Builds the URL for getting fields of a dataset.
     *
     * @param dataset the dataset name
     * @param version the version
     * @return the full URL
     */
    protected String buildFieldsUrl(String dataset, String version) {
        String path = String.format(RequestPath.GET_FIELDS, dataset, version);
        return buildUrl(path);
    }
    
    /**
     * Builds the URL for searching records in a dataset.
     *
     * @param dataset the dataset name
     * @param version the version
     * @return the full URL
     */
    protected String buildSearchUrl(String dataset, String version) {
        String path = String.format(RequestPath.SEARCH_RECORDS, dataset, version);
        return buildUrl(path);
    }
    
    /**
     * Logs the request details.
     *
     * @param method the HTTP method
     * @param url the request URL
     */
    protected void logRequest(String method, String url) {
        log.info("=== REQUEST ===");
        log.info("Method: {}", method);
        log.info("URL: {}", url);
    }
    
    /**
     * Logs the request details with body.
     *
     * @param method the HTTP method
     * @param url the request URL
     * @param body the request body
     */
    protected void logRequest(String method, String url, String body) {
        logRequest(method, url);
        log.info("Body: {}", body);
    }
    
    /**
     * Logs the response details.
     *
     * @param statusCode the response status code
     * @param body the response body
     */
    protected void logResponse(int statusCode, String body) {
        log.info("=== RESPONSE ===");
        log.info("Status Code: {}", statusCode);
        log.info("Body: {}", body);
    }
}
