package com.uspto.api.tests.base;

import com.uspto.api.config.ConfigurationManager;
import com.uspto.api.services.DataSetService;
import com.uspto.api.services.SearchService;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Base test class providing common setup and utilities for all tests.
 * 
 * @author USPTO API Test Team
 * @version 1.0
 */
@Slf4j
public abstract class BaseTest {

    protected ConfigurationManager config;
    protected DataSetService dataSetService;
    protected SearchService searchService;

    @BeforeClass(alwaysRun = true)
    @Step("Initialize test configuration and services")
    public void setupClass() {
        log.info("Initializing test configuration and services");
        config = ConfigurationManager.getInstance();
        dataSetService = new DataSetService();
        searchService = new SearchService();
        log.info("Test configuration and services initialized successfully");
    }

    @BeforeMethod(alwaysRun = true)
    public void setupMethod(Method method) {
        log.info("========================================");
        log.info("Starting test method: {}", method.getName());
        log.info("========================================");
    }

    /**
     * Gets default dataset name from configuration.
     *
     * @return default dataset name
     */
    protected String getDefaultDataset() {
        return config.getDefaultDataset();
    }

    /**
     * Gets default version from configuration.
     *
     * @return default version
     */
    protected String getDefaultVersion() {
        return config.getDefaultVersion();
    }
}
