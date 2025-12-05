package com.klindziuk.staf.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.Duration;
import java.time.Instant;

/**
 * TestNG listener to capture test suite and test execution lifecycle events.
 * Provides detailed logging and statistics for test execution.
 */
@Slf4j
public class TestSuiteListener implements ISuiteListener, ITestListener {

    private Instant suiteStartTime;
    private Instant testStartTime;

    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = Instant.now();
        log.info("========================================");
        log.info("Starting Test Suite: {}", suite.getName());
        log.info("========================================");
    }

    @Override
    public void onFinish(ISuite suite) {
        Duration duration = Duration.between(suiteStartTime, Instant.now());
        log.info("========================================");
        log.info("Finished Test Suite: {}", suite.getName());
        log.info("Total execution time: {} seconds", duration.getSeconds());
        log.info("========================================");
    }

    @Override
    public void onStart(ITestContext context) {
        testStartTime = Instant.now();
        log.info("----------------------------------------");
        log.info("Starting Test: {}", context.getName());
        log.info("----------------------------------------");
    }

    @Override
    public void onFinish(ITestContext context) {
        Duration duration = Duration.between(testStartTime, Instant.now());
        
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        log.info("----------------------------------------");
        log.info("Finished Test: {}", context.getName());
        log.info("Test execution time: {} seconds", duration.getSeconds());
        log.info("Test Results - Total: {}, Passed: {}, Failed: {}, Skipped: {}", 
                 total, passed, failed, skipped);
        log.info("----------------------------------------");
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.debug("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.debug("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            log.error("Failure cause: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());
    }
}
