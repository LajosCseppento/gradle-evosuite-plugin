package com.github.cseppento.gradle.evosuite.tasks;

import com.github.cseppento.gradle.evosuite.EvoSuiteExtension;
import com.github.cseppento.gradle.evosuite.EvoSuitePlugin;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.JavaExec;

/**
 * Base task type for all the tasks which call EvoSuite, extends {@link JavaExec}.
 */
public abstract class EvoSuiteBaseTask extends JavaExec {
    public EvoSuiteBaseTask() {
        super();

        EvoSuiteExtension extension = (EvoSuiteExtension) getProject().getExtensions().findByName(EvoSuitePlugin.EXTENSION_NAME);
        if (extension == null) {
            String msg = String.format("The '%s' extension has not been applied", EvoSuitePlugin.EXTENSION_NAME);
            throw new IllegalStateException(msg);
        }

        Configuration config = getProject().getConfigurations().findByName(EvoSuitePlugin.TEST_GENERATION_CONFIG_NAME);
        if (config == null) {
            String msg = String.format("The '%s' configuration was not found", EvoSuitePlugin.TEST_GENERATION_CONFIG_NAME);
            throw new IllegalStateException(msg);
        }

        this.setClasspath(config);
        this.setMain(extension.getToolMainClassName());
    }
}
