package com.github.cseppento.gradle.evosuite.testprojects.simple;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Simple object SUT.
 */
public final class SimpleObjectSut {
    private final long value;
    private final SortedSet<Long> divisors;

    public SimpleObjectSut(long value) {
        this.value = Math.abs(value);
        this.divisors = new TreeSet<>();
    }

    public long getValue() {
        return value;
    }

    public SortedSet<Long> getDivisors() {
        return Collections.unmodifiableSortedSet(divisors);
    }

    /**
     * Guess a divisor of {@link #getValue()}.
     *
     * @param divisor a number which meant to be a divisor
     * @throws IllegalArgumentException for bad guess (non-positive number, already guessed, not a divisor)
     */
    public void guessDivisor(long divisor) {
        if (divisor <= 0) {
            throw new IllegalArgumentException("Invalid divisor: " + divisor);
        } else if (divisors.contains(divisor)) {
            throw new IllegalArgumentException(divisor + " is already added as a divisor of " + value);
        }

        if (value % divisor == 0) {
            divisors.add(divisor);
        } else {
            throw new IllegalArgumentException(divisor + " is not a divisor of " + value);
        }
    }

    /**
     * Check internal state.
     *
     * @throws IllegalStateException if inconsistency is found
     */
    public void check() {
        if (value < 0) {
            throw new IllegalStateException("WTF: how could it become 0? Dump: " + toString());
        }

        for (Long divisor : divisors) {
            if (divisor == null) {
                throw new IllegalStateException("WTF: how could it become null? Dump: " + toString());
            } else if (divisor < 0) {
                throw new IllegalStateException("WTF: how could it become negative? Dump: " + toString());
            } else if (value % divisor != 0) {
                throw new IllegalStateException("WTF: how could it become invalid? Dump: " + toString());
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s of %s with divisors %s", getClass().getSimpleName(), value, divisors);
    }
}
