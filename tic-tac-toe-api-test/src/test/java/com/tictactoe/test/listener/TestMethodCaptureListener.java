/*
 * Copyright (c) Tic Tac Toe Test Framework.
 */

package com.tictactoe.test.listener;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

/**
 * TestNG listener for capturing test method execution context.
 * Provides thread-safe access to current test method and result.
 */
public class TestMethodCaptureListener implements IInvokedMethodListener {
    private static final ThreadLocal<ITestNGMethod> currentMethods = new ThreadLocal<>();
    private static final ThreadLocal<ITestResult> currentResults = new ThreadLocal<>();

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        currentMethods.set(method.getTestMethod());
        currentResults.set(testResult);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        currentMethods.remove();
        currentResults.remove();
    }

    /**
     * Gets the current test method.
     *
     * @return the current ITestNGMethod
     * @throws RuntimeException if listener is not registered
     */
    public static ITestNGMethod getTestMethod() {
        return checkNotNull(
                currentMethods.get(),
                "Did you forget to register the %s listener?",
                TestMethodCaptureListener.class.getName());
    }

    /**
     * Gets the current test result.
     * Parameters passed from a data provider are accessible in the test result.
     *
     * @return the current ITestResult
     * @throws RuntimeException if listener is not registered
     */
    public static ITestResult getTestResult() {
        return checkNotNull(
                currentResults.get(),
                "Did you forget to register the %s listener?",
                TestMethodCaptureListener.class.getName());
    }

    private static <T> T checkNotNull(T object, String message, Object param) {
        if (object == null) {
            throw new RuntimeException(String.format(message, param));
        }
        return object;
    }
}
