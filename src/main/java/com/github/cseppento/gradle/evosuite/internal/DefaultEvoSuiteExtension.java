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
    private String toolMainClassName;

    public DefaultEvoSuiteExtension(Project project, String toolVersion, String toolMainClassName) {
        Objects.requireNonNull(project);
        Objects.requireNonNull(toolVersion);
        Objects.requireNonNull(toolMainClassName);

        this.project = project;
        this.toolVersion = toolVersion;
        this.toolMainClassName = toolMainClassName;
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

    @Override
    public String getToolMainClassName() {
        return toolMainClassName;
    }

    @Override
    public void setToolMainClassName(String toolMainClassName) {
        Objects.requireNonNull(toolMainClassName);
        this.toolMainClassName = toolMainClassName;
    }
}
