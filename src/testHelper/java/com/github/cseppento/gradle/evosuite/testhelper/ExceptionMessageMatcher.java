package com.github.cseppento.gradle.evosuite.testhelper;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;

/**
 * {@link Matcher} {@link Exception#getMessage()}.
 */
public class ExceptionMessageMatcher extends ObjectFieldMatcher<Throwable, String> {
    protected ExceptionMessageMatcher(Matcher<String> messageMatcher) {
        super("message", Throwable::getMessage, messageMatcher);
    }

    @Factory
    public static ExceptionMessageMatcher messageContainsString(String substring) {
        return new ExceptionMessageMatcher(StringContains.containsString(substring));
    }
}
