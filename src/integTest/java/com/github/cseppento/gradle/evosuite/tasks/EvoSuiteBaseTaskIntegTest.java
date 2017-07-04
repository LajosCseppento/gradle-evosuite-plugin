package com.github.cseppento.gradle.evosuite.tasks;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

@SuppressWarnings("squid:S2187") // all necessary tests are inherited
public class EvoSuiteBaseTaskIntegTest extends TaskIntegTestBase<EvoSuiteBaseTask> {
    public EvoSuiteBaseTaskIntegTest() {
        super(EvoSuiteBaseTaskSut.class);
    }

    @Override
    void checkCreateDefault() {
        assertThat(sut.getArgs(), empty());
    }

    public static class EvoSuiteBaseTaskSut extends EvoSuiteBaseTask {
        // extends with defaults
    }
}
