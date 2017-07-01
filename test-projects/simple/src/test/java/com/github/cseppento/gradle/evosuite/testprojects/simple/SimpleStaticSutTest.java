package com.github.cseppento.gradle.evosuite.testprojects.simple;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleStaticSutTest {
    @Test
    public void testIsSecretQuadraticEquationSolution_false() {
        assertFalse(SimpleStaticSut.isSecretQuadraticEquationSolution(0));
    }

    @Test
    public void testIsSecretQuadraticEquationSolution_true() {
        assertTrue(SimpleStaticSut.isSecretQuadraticEquationSolution(2));
    }

    @Test(expected = ArithmeticException.class)
    public void testIsSecretQuadraticEquationSolution_overflow() {
        SimpleStaticSut.isSecretQuadraticEquationSolution(Long.MAX_VALUE);
    }
}
