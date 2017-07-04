package com.github.cseppento.gradle.evosuite.tasks;

/**
 * Executes EvoSuite with the '-listParameters' argument.
 */
public class EvoSuiteParameters extends EvoSuiteBaseTask {
    public EvoSuiteParameters() {
        super();
        this.args("-listParameters");
    }
}
