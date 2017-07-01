package com.github.cseppento.gradle.evosuite.testprojects.duckduckgo;

/**
 * Exception class for {@link DuckDuckGoClient}.
 */
public class DuckDuckGoClientException extends Exception {
    public DuckDuckGoClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
