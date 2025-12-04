package com.tictactoe.api.base;

import com.tictactoe.api.clients.BoardApiClient;
import com.tictactoe.api.clients.SquareApiClient;
import com.tictactoe.api.utils.RestAssuredConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base test class for all API tests.
 * Provides common setup and teardown logic.
 */
@Slf4j
public abstract class BaseTest {
    protected BoardApiClient boardApiClient;
    protected SquareApiClient squareApiClient;

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        log.info("Initializing test suite");
        RestAssuredConfig.init();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        log.info("Setting up test");
        boardApiClient = new BoardApiClient();
        squareApiClient = new SquareApiClient();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Tearing down test");
    }
}
