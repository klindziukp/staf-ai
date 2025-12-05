package com.staf.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.Duration;
import java.time.Instant;

/**
 * TestNG listener to capture test suite and test execution details.
 * Provides comprehensive logging for test suite lifecycle.
 */
@Slf4j
public class TestSuiteListener implements ISuiteListener, ITestListener {

    private Instant suiteStartTime;
    private Instant testStartTime;

    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = Instant.now();
        log.info("╔════════════════════════════════════════════════════════════════╗");
        log.info("║           TEST SUITE EXECUTION STARTED                         ║");
        log.info("╚════════════════════════════════════════════════════════════════╝");
        log.info("Suite Name: {}", suite.getName());
        log.info("Start Time: {}", suiteStartTime);
        log.info("================================================================");
    }

    @Override
    public void onFinish(ISuite suite) {
        Instant suiteEndTime = Instant.now();
        Duration duration = Duration.between(suiteStartTime, suiteEndTime);
        
        log.info("================================================================");
        log.info("╔════════════════════════════════════════════════════════════════╗");
        log.info("║           TEST SUITE EXECUTION COMPLETED                       ║");
        log.info("╚════════════════════════════════════════════════════════════════╝");
        log.info("Suite Name: {}", suite.getName());
        log.info("End Time: {}", suiteEndTime);
        log.info("Total Duration: {} seconds ({} ms)", 
                duration.getSeconds(), duration.toMillis());
        log.info("================================================================");
    }

    @Override
    public void onTestStart(ITestContext context) {
        testStartTime = Instant.now();
        log.info("┌────────────────────────────────────────────────────────────────┐");
        log.info("│ Test Started: {}", context.getName());
        log.info("└────────────────────────────────────────────────────────────────┘");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✓ TEST PASSED: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("✗ TEST FAILED: {}", result.getName());
        if (result.getThrowable() != null) {
            log.error("Failure reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⊘ TEST SKIPPED: {}", result.getName());
        if (result.getThrowable() != null) {
            log.warn("Skip reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        Instant testEndTime = Instant.now();
        Duration duration = Duration.between(testStartTime, testEndTime);
        
        log.info("┌────────────────────────────────────────────────────────────────┐");
        log.info("│ Test Finished: {}", context.getName());
        log.info("│ Duration: {} ms", duration.toMillis());
        log.info("│ Passed: {}", context.getPassedTests().size());
        log.info("│ Failed: {}", context.getFailedTests().size());
        log.info("│ Skipped: {}", context.getSkippedTests().size());
        log.info("└────────────────────────────────────────────────────────────────┘");
    }
}
