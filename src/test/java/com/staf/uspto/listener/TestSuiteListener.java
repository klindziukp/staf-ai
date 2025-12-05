package com.staf.uspto.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.Duration;
import java.time.Instant;

/**
 * TestNG listener to capture and log test suite and test execution details.
 * This listener tracks the overall test execution lifecycle.
 */
@Slf4j
public class TestSuiteListener implements ISuiteListener, ITestListener {
    
    private static final String SUITE_START = "########## Starting Test Suite: {} ##########";
    private static final String SUITE_END = "########## Finished Test Suite: {} ##########";
    private static final String SUITE_DURATION = "Total Suite Duration: {} seconds";
    private static final String TEST_START = "========== Starting Test: {} ==========";
    private static final String TEST_END = "========== Finished Test: {} ==========";
    private static final String TEST_SUMMARY = "Test Summary - Passed: {}, Failed: {}, Skipped: {}";
    
    private Instant suiteStartTime;
    
    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = Instant.now();
        log.info(SUITE_START, suite.getName());
        log.info("Suite XML File: {}", suite.getXmlSuite().getFileName());
    }
    
    @Override
    public void onFinish(ISuite suite) {
        Duration duration = Duration.between(suiteStartTime, Instant.now());
        log.info(SUITE_DURATION, duration.getSeconds());
        log.info(SUITE_END, suite.getName());
    }
    
    @Override
    public void onStart(ITestContext context) {
        log.info(TEST_START, context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        log.info(TEST_SUMMARY, passed, failed, skipped);
        log.info(TEST_END, context.getName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✓ Test PASSED: {}", result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        log.error("✗ Test FAILED: {}", result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            log.error("Failure reason: {}", result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⊘ Test SKIPPED: {}", result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());
    }
}
