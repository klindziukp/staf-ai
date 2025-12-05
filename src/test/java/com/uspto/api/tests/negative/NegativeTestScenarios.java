package com.uspto.api.tests.negative;

import com.uspto.api.constants.HttpStatusCodes;
import com.uspto.api.models.SearchRequest;
import com.uspto.api.tests.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Negative test scenarios for USPTO Data Set API.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Epic("USPTO Data Set API")
@Feature("Negative Testing")
public class NegativeTestScenarios extends BaseTest {

    @Test(description = "Verify list fields with empty dataset name")
    @Story("Invalid input handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles empty dataset name appropriately")
    public void testListFields_EmptyDatasetName() {
        // Execute API call with empty dataset
        Response response = dataSetService.listFields("", "v1");

        // Validate status code is 404 or 400
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 or 400 for empty dataset")
                .isIn(HttpStatusCodes.NOT_FOUND, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify list fields with empty version")
    @Story("Invalid input handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles empty version appropriately")
    public void testListFields_EmptyVersion() {
        // Execute API call with empty version
        Response response = dataSetService.listFields(getDefaultDataset(), "");

        // Validate status code is 404 or 400
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 or 400 for empty version")
                .isIn(HttpStatusCodes.NOT_FOUND, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify search with null criteria")
    @Story("Invalid search criteria handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles null search criteria")
    public void testSearchRecords_NullCriteria() {
        // Create search request with null criteria
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria(null)
                .start(0)
                .rows(10)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate response - should either accept null as wildcard or return error
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 200 or 400")
                .isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify search with negative start value")
    @Story("Invalid pagination parameters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles negative start value")
    public void testSearchRecords_NegativeStartValue() {
        // Create search request with negative start
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(-1)
                .rows(10)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate response
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 200 or 400 for negative start")
                .isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify search with negative rows value")
    @Story("Invalid pagination parameters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles negative rows value")
    public void testSearchRecords_NegativeRowsValue() {
        // Create search request with negative rows
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(-10)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate response
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 200 or 400 for negative rows")
                .isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify search with extremely large start value")
    @Story("Boundary value testing")
    @Severity(SeverityLevel.MINOR)
    @Description("Test verifies API handles extremely large start value")
    public void testSearchRecords_ExtremelyLargeStartValue() {
        // Create search request with extremely large start
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(Integer.MAX_VALUE)
                .rows(10)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate response - should handle gracefully
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 200 or 400")
                .isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify list fields with SQL injection attempt")
    @Story("Security testing")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies API is protected against SQL injection")
    public void testListFields_SqlInjectionAttempt() {
        // Execute API call with SQL injection attempt
        Response response = dataSetService.listFields("test' OR '1'='1", "v1");

        // Validate status code is 404 or 400 (not 500)
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 or 400, not 500")
                .isIn(HttpStatusCodes.NOT_FOUND, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify search with malformed JSON criteria")
    @Story("Invalid input handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles malformed search criteria")
    public void testSearchRecords_MalformedCriteria() {
        // Create search request with malformed criteria
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("}{invalid json}{")
                .start(0)
                .rows(10)
                .build();

        // Execute API call
        Response response = searchService.searchRecords(
                getDefaultDataset(),
                getDefaultVersion(),
                searchRequest
        );

        // Validate response
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 200 or 400")
                .isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify list fields with very long dataset name")
    @Story("Boundary value testing")
    @Severity(SeverityLevel.MINOR)
    @Description("Test verifies API handles very long dataset name")
    public void testListFields_VeryLongDatasetName() {
        // Create very long dataset name
        String longDatasetName = "a".repeat(1000);

        // Execute API call
        Response response = dataSetService.listFields(longDatasetName, "v1");

        // Validate status code is 404 or 400
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 or 400 for very long dataset name")
                .isIn(HttpStatusCodes.NOT_FOUND, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify search with invalid dataset and version combination")
    @Story("Invalid input combination")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies API handles invalid dataset and version combination")
    public void testSearchRecords_InvalidDatasetVersionCombination() {
        // Create search request
        SearchRequest searchRequest = SearchRequest.builder()
                .criteria("*:*")
                .start(0)
                .rows(10)
                .build();

        // Execute API call with invalid combination
        Response response = searchService.searchRecords(
                "invalid_dataset",
                "invalid_version",
                searchRequest
        );

        // Validate status code is 404
        int statusCode = response.getStatusCode();
        assertThat(statusCode)
                .as("Status code should be 404 for invalid dataset/version combination")
                .isEqualTo(HttpStatusCodes.NOT_FOUND);
    }
}
