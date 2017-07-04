package com.github.cseppento.gradle.evosuite.tasks;

/**
 * Executes EvoSuite with the '-help' argument.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth") // only 2 levels of inheritance from the Gradle API
public class EvoSuiteHelp extends EvoSuiteBaseTask {
    public EvoSuiteHelp() {
        super();
        this.args("-help");
    }
}
