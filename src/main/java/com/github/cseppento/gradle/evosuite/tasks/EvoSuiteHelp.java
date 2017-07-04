package com.github.cseppento.gradle.evosuite.tasks;

/**
 * Executes EvoSuite with the '-help' argument.
 */
public class EvoSuiteHelp extends EvoSuiteBaseTask {
    public EvoSuiteHelp() {
        super();
        this.args("-help");
    }
}
