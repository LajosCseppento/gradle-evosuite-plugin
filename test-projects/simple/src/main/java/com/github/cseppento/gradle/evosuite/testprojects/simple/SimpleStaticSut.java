package com.github.cseppento.gradle.evosuite.testprojects.simple;

/**
 * Simple static SUT.
 */
public final class SimpleStaticSut {
    private SimpleStaticSut() {
        throw new UnsupportedOperationException("Static class");
    }

    /**
     * Tells whether the parameter is a solution of the quadratic equation held by this method.
     *
     * @param x guessed value
     * @return <code>true</code> if the parameter satisfies the equation, otherwise <code>false</code>
     * @throws ArithmeticException if overflow occurs
     */
    @SuppressWarnings("squid:S1126") // keep longer if-else for better coverage visualization
    public static boolean isSecretQuadraticEquationSolution(long x) {
        if (Math.multiplyExact(x, x) - Math.multiplyExact(3, x) + 2 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
