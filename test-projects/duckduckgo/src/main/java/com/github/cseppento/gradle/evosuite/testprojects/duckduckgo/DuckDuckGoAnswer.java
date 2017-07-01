package com.github.cseppento.gradle.evosuite.testprojects.duckduckgo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO object in which JSON result is parsed during {@link DuckDuckGoClient#search(String)}.
 */
public final class DuckDuckGoAnswer {
    private final String heading;
    private final String abstractUrl;

    @JsonCreator
    public DuckDuckGoAnswer(@JsonProperty("Heading") String heading, @JsonProperty("AbstractURL") String abstractUrl) {
        this.heading = heading;
        this.abstractUrl = abstractUrl;
    }

    public String getHeading() {
        return heading;
    }

    public String getAbstractUrl() {
        return abstractUrl;
    }

    @Override
    public String toString() {
        return String.format("%s{heading=%s, abstractUrl=%s}", getClass().getSimpleName(), heading, abstractUrl);
    }
}
