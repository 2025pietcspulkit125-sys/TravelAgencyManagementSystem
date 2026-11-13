package com.example.travelagency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Loads database settings from environment variables, JVM properties, or .env. */
public final class DatabaseConfig {

    private static final Map<String, String> DOT_ENV = loadDotEnv();

    private DatabaseConfig() {
    }

    public static String require(String environmentName, String propertyName) {
        String value = optional(environmentName, propertyName);
        if (value == null) {
            throw new IllegalStateException(
                    "Set " + environmentName + " or -D" + propertyName + " (or add it to .env)");
        }
        return value;
    }

    public static String optional(String environmentName, String propertyName) {
        String value = System.getenv(environmentName);
        if (isPresent(value)) {
            return value.trim();
        }

        value = System.getProperty(propertyName);
        if (isPresent(value)) {
            return value.trim();
        }

        value = DOT_ENV.get(environmentName);
        return isPresent(value) ? value.trim() : null;
    }

    public static String valueOrDefault(
            String environmentName, String propertyName, String defaultValue) {
        String value = optional(environmentName, propertyName);
        return value == null ? defaultValue : value;
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    private static Map<String, String> loadDotEnv() {
        Map<String, String> values = new HashMap<>();
        Path envFile = Path.of(System.getProperty("user.dir"), ".env");
        if (!Files.isRegularFile(envFile)) {
            return values;
        }

        try {
            for (String line : Files.readAllLines(envFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                    continue;
                }
                int separator = trimmed.indexOf('=');
                String key = trimmed.substring(0, separator).trim();
                String value = trimmed.substring(separator + 1).trim();
                if ((value.startsWith("\"") && value.endsWith("\""))
                        || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                if (!key.isEmpty()) {
                    values.put(key, value);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read .env", exception);
        }
        return values;
    }
}
