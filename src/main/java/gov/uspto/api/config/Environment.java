package gov.uspto.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Enum representing different test environments
 */
@Getter
@RequiredArgsConstructor
public enum Environment {
    DEV("dev"),
    STAGING("staging"),
    PROD("prod");

    private final String name;

    public static Environment fromString(String envName) {
        return Arrays.stream(values())
                .filter(env -> env.getName().equalsIgnoreCase(envName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown environment: " + envName + ". Valid values: " + Arrays.toString(values())
                ));
    }
}
