package com.github.cseppento.gradle.evosuite;

/**
 * This extension allows the user to configure the {@link EvoSuiteGradlePlugin}.
 */
public interface EvoSuiteGradleExtension {
    /**
     * @return Version of EvoSuite to use
     */
    String getToolVersion();

    /**
     * @param toolVersion Version of Evosuite to use
     */
    void setToolVersion(String toolVersion);
}
