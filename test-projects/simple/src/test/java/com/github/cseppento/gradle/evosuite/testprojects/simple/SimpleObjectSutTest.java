package com.github.cseppento.gradle.evosuite.testprojects.simple;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleObjectSutTest {
    @Test
    public void testSmoke() {
        SimpleObjectSut sut = new SimpleObjectSut(100);

        assertEquals(100, sut.getValue());
        assertTrue(sut.getDivisors().isEmpty());

        sut.guessDivisor(1);
        sut.guessDivisor(2);
        sut.guessDivisor(5);

        assertEquals(100, sut.getValue());
        assertArrayEquals(new Object[]{1L, 2L, 5L}, sut.getDivisors().toArray());
    }
}
