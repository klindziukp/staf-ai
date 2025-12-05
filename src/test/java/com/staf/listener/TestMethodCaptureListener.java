package com.staf.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * TestNG listener to capture test method execution details.
 * Logs test method start, finish, and execution time.
 */
@Slf4j
public class TestMethodCaptureListener implements IInvokedMethodListener {

    private final Map<String, Instant> methodStartTimes = new HashMap<>();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = getMethodName(method, testResult);
            methodStartTimes.put(methodName, Instant.now());
            log.info("========================================");
            log.info("Starting test method: {}", methodName);
            log.info("Test class: {}", testResult.getTestClass().getName());
            log.info("========================================");
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            String methodName = getMethodName(method, testResult);
            Instant startTime = methodStartTimes.get(methodName);
            
            if (startTime != null) {
                Duration duration = Duration.between(startTime, Instant.now());
                String status = getTestStatus(testResult);
                
                log.info("========================================");
                log.info("Finished test method: {}", methodName);
                log.info("Status: {}", status);
                log.info("Execution time: {} ms", duration.toMillis());
                
                if (testResult.getThrowable() != null) {
                    log.error("Test failed with exception:", testResult.getThrowable());
                }
                
                log.info("========================================");
                methodStartTimes.remove(methodName);
            }
        }
    }

    /**
     * Get formatted method name.
     *
     * @param method     invoked method
     * @param testResult test result
     * @return formatted method name
     */
    private String getMethodName(IInvokedMethod method, ITestResult testResult) {
        return testResult.getTestClass().getName() + "." + method.getTestMethod().getMethodName();
    }

    /**
     * Get test status as string.
     *
     * @param testResult test result
     * @return status string
     */
    private String getTestStatus(ITestResult testResult) {
        return switch (testResult.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
