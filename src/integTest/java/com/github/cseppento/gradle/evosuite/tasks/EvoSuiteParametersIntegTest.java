package com.github.cseppento.gradle.evosuite.tasks;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@SuppressWarnings("squid:S2187") // all necessary tests are inherited
public class EvoSuiteParametersIntegTest extends TaskIntegTestBase<EvoSuiteParameters> {
    public EvoSuiteParametersIntegTest() {
        super(EvoSuiteParameters.class);
    }

    @Override
    void checkCreateDefault() {
        assertThat(sut.getArgs(), contains("-listParameters"));
    }
}
