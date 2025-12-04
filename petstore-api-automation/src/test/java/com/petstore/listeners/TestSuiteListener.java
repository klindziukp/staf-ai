package com.petstore.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for suite and test lifecycle events
 * Provides logging for test execution tracking
 */
@Slf4j
public final class TestSuiteListener implements ISuiteListener, ITestListener {
    
    @Override
    public void onStart(final ISuite suite) {
        log.info("========================================");
        log.info("TEST SUITE [{}] STARTED", suite.getName());
        log.info("========================================");
    }
    
    @Override
    public void onFinish(final ISuite suite) {
        log.info("========================================");
        log.info("TEST SUITE [{}] FINISHED", suite.getName());
        log.info("========================================");
    }
    
    @Override
    public void onTestStart(final ITestResult result) {
        log.info(">>> TEST METHOD {} STARTED >>>", testName(result));
    }
    
    @Override
    public void onTestSuccess(final ITestResult result) {
        log.info(">>> TEST METHOD {} PASSED >>>", testName(result));
    }
    
    @Override
    public void onTestFailure(final ITestResult result) {
        log.error(">>> TEST METHOD {} FAILED >>>", testName(result));
        log.error("Failure reason: ", result.getThrowable());
    }
    
    @Override
    public void onTestSkipped(final ITestResult result) {
        log.warn(">>> TEST METHOD {} SKIPPED >>>", testName(result));
    }
    
    private String testName(final ITestResult testResult) {
        return String.format(
                "[%s.%s]",
                testResult.getTestClass().getName(),
                testResult.getMethod().getMethodName());
    }
}
