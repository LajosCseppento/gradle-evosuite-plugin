package com.github.cseppento.gradle.evosuite.tasks;

/**
 * Executes EvoSuite with the '-listParameters' argument.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth") // only 2 levels of inheritance from the Gradle API
public class EvoSuiteParameters extends EvoSuiteBaseTask {
    public EvoSuiteParameters() {
        super();
        this.args("-listParameters");
    }
}
