/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Base suite and test listeners for TestNG configuration.
 * Required to start test creation using TestNG.
 * Provides logging for test suite and test method lifecycle events.
 */
@Slf4j
public final class TestSuiteListener implements ISuiteListener, ITestListener {

    @Override
    public void onStart(final ISuite suite) {
        log.info("---> TEST SUITE [{}] STARTED --->", suite.getName());
    }

    @Override
    public void onFinish(final ISuite suite) {
        log.info("---> TEST SUITE [{}] FINISHED --->", suite.getName());
    }

    @Override
    public void onTestStart(final ITestResult result) {
        log.info("---> TEST METHOD {} STARTED --->", testName(result));
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
        log.info("---> TEST METHOD {} SUCCESSFULLY FINISHED --->", testName(result));
        onTestFinish();
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        log.error("---> TEST METHOD {} FAILED --->", testName(result));
        onTestFinish();
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
        log.warn("---> TEST METHOD {} SKIPPED --->", testName(result));
        onTestFinish();
    }

    /**
     * Method drives test's lifecycle.
     * It is easier to scale in future in case tests launch by test method.
     */
    private void onTestFinish() {
        // Hook for future enhancements
    }

    private String testName(final ITestResult testResult) {
        return String.format(
                "[%s.%s]",
                testResult.getTestClass().getName(),
                testResult.getMethod().getMethodName());
    }
}
