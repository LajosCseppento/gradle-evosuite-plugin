package com.github.cseppento.gradle.evosuite.internal;

import org.gradle.api.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEvoSuiteExtensionTest {
    @Mock
    private Project project;

    private DefaultEvoSuiteExtension sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new DefaultEvoSuiteExtension(project, "DEFAULT_VER", "DEFAULT_MAIN");
    }

    @Test
    public void testGetToolVersion() {
        assertEquals("DEFAULT_VER", sut.getToolVersion());
    }

    @Test
    public void testSetToolVersion() {
        sut.setToolVersion("OTHER");
        assertEquals("OTHER", sut.getToolVersion());
    }

    @Test(expected = NullPointerException.class)
    public void testSetToolVersionFailsIfNull() {
        sut.setToolVersion(null);
    }
}
