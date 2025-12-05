package com.klindziuk.staf.test;

import com.klindziuk.staf.constant.RequestPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Base test class for USPTO Data Set API tests.
 * Provides common HTTP client setup and utility methods.
 */
@Slf4j
public abstract class BaseApiTest {

    protected CloseableHttpClient httpClient;
    protected String baseUrl;

    @BeforeClass
    public void setUp() {
        log.info("Setting up test environment");
        baseUrl = RequestPath.BASE_URL;
        httpClient = HttpClients.createDefault();
        log.info("Base URL: {}", baseUrl);
    }

    @AfterClass
    public void tearDown() {
        log.info("Tearing down test environment");
        if (httpClient != null) {
            try {
                httpClient.close();
                log.info("HTTP client closed successfully");
            } catch (IOException e) {
                log.error("Failed to close HTTP client", e);
            }
        }
    }

    /**
     * Executes a GET request to the specified path.
     *
     * @param path the request path
     * @return the HTTP response
     * @throws IOException if the request fails
     */
    protected ClassicHttpResponse executeGet(String path) throws IOException {
        String url = baseUrl + path;
        log.info("Executing GET request to: {}", url);
        
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        
        return httpClient.execute(request);
    }

    /**
     * Executes a POST request with form data.
     *
     * @param path        the request path
     * @param formData    the form data as string
     * @return the HTTP response
     * @throws IOException if the request fails
     */
    protected ClassicHttpResponse executePost(String path, String formData) throws IOException {
        String url = baseUrl + path;
        log.info("Executing POST request to: {}", url);
        log.debug("Form data: {}", formData);
        
        HttpPost request = new HttpPost(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setEntity(new StringEntity(formData, ContentType.APPLICATION_FORM_URLENCODED));
        
        return httpClient.execute(request);
    }

    /**
     * Builds form data string from parameters.
     *
     * @param criteria the search criteria
     * @param start    the start index
     * @param rows     the number of rows
     * @return the form data string
     */
    protected String buildFormData(String criteria, int start, int rows) {
        try {
            return String.format("criteria=%s&start=%d&rows=%d",
                    URLEncoder.encode(criteria, StandardCharsets.UTF_8),
                    start,
                    rows);
        } catch (Exception e) {
            log.error("Failed to build form data", e);
            throw new RuntimeException("Failed to build form data", e);
        }
    }

    /**
     * Builds the fields endpoint path for a dataset.
     *
     * @param dataset the dataset name
     * @param version the API version
     * @return the fields endpoint path
     */
    protected String buildFieldsPath(String dataset, String version) {
        return String.format(RequestPath.FIELDS, dataset, version);
    }

    /**
     * Builds the records endpoint path for a dataset.
     *
     * @param dataset the dataset name
     * @param version the API version
     * @return the records endpoint path
     */
    protected String buildRecordsPath(String dataset, String version) {
        return String.format(RequestPath.RECORDS, dataset, version);
    }
}
