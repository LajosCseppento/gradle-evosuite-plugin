package com.github.cseppento.gradle.evosuite.internal;

import com.github.cseppento.gradle.evosuite.EvoSuiteGradleExtension;
import org.gradle.api.Project;

import java.util.Objects;

/**
 * Default implementation of {@link EvoSuiteGradleExtension}.
 */
public class DefaultEvoSuiteGradleExtension implements EvoSuiteGradleExtension {
    private final Project project;
    private String toolVersion;

    public DefaultEvoSuiteGradleExtension(Project project, String toolVersion) {
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
