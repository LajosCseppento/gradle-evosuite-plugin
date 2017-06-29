package com.github.cseppento.gradle.evosuite.testhelper;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;
import java.util.function.Function;

/**
 * Generalised {@link Matcher} for object fields.
 *
 * @param <T> type of the object
 * @param <R> type of the field
 */
public class ObjectFieldMatcher<T, R> extends TypeSafeMatcher<T> {
    private final String fieldName;
    private final Function<T, R> fieldExtractor;
    private final Matcher<R> fieldMatcher;

    protected ObjectFieldMatcher(String fieldName,
                                 Function<T, R> fieldExtractor,
                                 Matcher<R> fieldMatcher) {
        this.fieldName = fieldName;
        this.fieldExtractor = Objects.requireNonNull(fieldExtractor);
        this.fieldMatcher = Objects.requireNonNull(fieldMatcher);
    }

    @Override
    protected boolean matchesSafely(T item) {
        return item != null && fieldMatcher.matches(fieldExtractor.apply(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(fieldName == null ? "field" : fieldName);
        description.appendText(" is expected: ");
        fieldMatcher.describeTo(description);
    }

    @Factory
    public static <T, R> ObjectFieldMatcher<T, R> fieldMatches(Function<T, R> fieldExtractor,
                                                               Matcher<R> fieldMatcher) {
        return fieldMatches(null, fieldExtractor, fieldMatcher);
    }

    @Factory
    public static <T, R> ObjectFieldMatcher<T, R> fieldMatches(String fieldName,
                                                               Function<T, R> fieldExtractor,
                                                               Matcher<R> fieldMatcher) {
        return new ObjectFieldMatcher<>(fieldName, fieldExtractor, fieldMatcher);
    }
}
