package com.github.cseppento.gradle.evosuite.internal;

import com.github.cseppento.gradle.evosuite.EvoSuiteExtension;
import org.gradle.api.Project;

import java.util.Objects;

/**
 * Default implementation of {@link EvoSuiteExtension}.
 */
public class DefaultEvoSuiteExtension implements EvoSuiteExtension {
    private final Project project;
    private String toolVersion;

    public DefaultEvoSuiteExtension(Project project, String toolVersion) {
        Objects.requireNonNull(project);
        Objects.requireNonNull(toolVersion);

        this.project = project;
        this.toolVersion = toolVersion;
    }

    @Override
    public String getToolVersion() {
        return toolVersion;
    }

    @Override
    public void setToolVersion(String toolVersion) {
        Objects.requireNonNull(toolVersion);
        this.toolVersion = toolVersion;
    }
}
