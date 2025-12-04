package com.klindziuk.petstore.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing different environments for the Petstore API
 */
@Getter
@RequiredArgsConstructor
public enum Environment {
    DEV("http://petstore.swagger.io/v1"),
    QA("http://petstore.swagger.io/v1"),
    PROD("http://petstore.swagger.io/v1");
    
    private final String baseUrl;
}
