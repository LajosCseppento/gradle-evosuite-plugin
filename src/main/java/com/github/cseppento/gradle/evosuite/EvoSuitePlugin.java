package com.github.cseppento.gradle.evosuite;

import com.github.cseppento.gradle.evosuite.internal.DefaultEvoSuiteExtension;
import com.github.cseppento.gradle.evosuite.tasks.EvoSuiteHelp;
import com.github.cseppento.gradle.evosuite.tasks.EvoSuiteParameters;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.testing.Test;

import java.util.Objects;

/**
 * This plugin integrates EvoSuite test generation and execution with Gradle.
 */
public class EvoSuitePlugin implements Plugin<Project> {
    private static final String DEFAULT_TOOL_VERSION = "1.0.5";
    private static final String DEFAULT_TOOL_MAIN_CLASS_NAME = "org.evosuite.EvoSuite";

    public static final String EXTENSION_NAME = "evosuite";
    public static final String TEST_GENERATION_CONFIG_NAME = "evosuiteGeneration";
    private static final String SOURCE_SET_NAME = "evosuiteTest";
    private static final String SOURCE_SET_DIR_NAME = "evoTest";

    private Project project;
    private Logger logger;
    private TaskContainer tasks;
    private SourceSetContainer sourceSets;

    private EvoSuiteExtension extension;
    private Configuration evosuiteGenerateConfig;

    private SourceSet sourceSet;

    @Override
    public void apply(Project project) {
        Objects.requireNonNull(project);
        this.project = project;
        this.logger = project.getLogger();
        this.tasks = project.getTasks();

        if (project.getExtensions().findByName(EXTENSION_NAME) != null) {
            logger.info("The {] extension has been already applied", EXTENSION_NAME);
            return;
        }
        if (project.getPluginManager().findPlugin("java") == null) {
            throw new GradleException("Please apply the java plugin first in order to use the EvoSuite plugin");
        }

        this.sourceSets = project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets();

        extension = project.getExtensions().create(
                EvoSuiteExtension.class, EXTENSION_NAME, DefaultEvoSuiteExtension.class,
                project, DEFAULT_TOOL_VERSION, DEFAULT_TOOL_MAIN_CLASS_NAME);

        evosuiteGenerateConfig = project.getConfigurations().create(TEST_GENERATION_CONFIG_NAME);
        evosuiteGenerateConfig.defaultDependencies(depSet -> {
            String depGav = "org.evosuite:evosuite-master:" + extension.getToolVersion();
            Dependency dep = project.getDependencies().create(depGav);
            depSet.add(dep);
        });

        createSourceSet();

        createTasks();

        project.getConfigurations().getByName(sourceSet.getCompileConfigurationName()).defaultDependencies(depSet -> {
            String depGav = "org.evosuite:evosuite-client:" + extension.getToolVersion();
            Dependency dep = project.getDependencies().create(depGav);
            depSet.add(dep);
        });

        // TODO detect IDE plugins (even if applied later) and add sourceSet to IDE config
    }

    private void createSourceSet() {
        sourceSet = sourceSets.create(SOURCE_SET_NAME);

        sourceSet.getJava().setSrcDirs(project.files(String.format("src/%s/java", SOURCE_SET_DIR_NAME)));
        sourceSet.getResources().setSrcDirs(project.files(String.format("src/%s/resources", SOURCE_SET_DIR_NAME)));

        SourceSetOutput mainOutput = sourceSets.getByName("main").getOutput();
        sourceSet.setCompileClasspath(project.files(sourceSet.getCompileClasspath(), mainOutput));
        sourceSet.setRuntimeClasspath(project.files(sourceSet.getRuntimeClasspath(), mainOutput));
    }

    private void createTasks() {
        // TODO task evosuiteListClasses

        tasks.create("evosuiteHelp", EvoSuiteHelp.class, task -> {
            task.setGroup("EvoSuite");
            task.setDescription("Displays EvoSuite help.");
        });

        tasks.create("evosuiteParameters", EvoSuiteParameters.class, task -> {
            task.setGroup("EvoSuite");
            task.setDescription("Displays EvoSuite parameters.");
        });

        // Task: evosuiteGenerateTests : JavaExec
        JavaExec genTask = tasks.create("evosuiteGenerateTests", JavaExec.class);
        genTask.setGroup("EvoSuite");
        genTask.setDescription("Generates tests using EvoSuite.");
        genTask.dependsOn(tasks.getByName("classes"));
        genTask.setClasspath(evosuiteGenerateConfig);
        genTask.setMain("org.evosuite.EvoSuite");

        String projectClasspath = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getOutput().getClassesDirs().getAsPath();
        genTask.args("-target", projectClasspath,
                "-projectCP", projectClasspath,
                "-generateSuite",
                "-Dtest_dir=" + sourceSet.getJava().getSrcDirs().iterator().next()
        );

        // Task: evosuiteTest : Test
        Test evosuiteTest = tasks.create("evosuiteTest", Test.class);
        evosuiteTest.setGroup("EvoSuite");
        evosuiteTest.setDescription("Runs the tests generated by EvoSuite.");

        evosuiteTest.setTestClassesDirs(sourceSet.getOutput().getClassesDirs());
        evosuiteTest.setClasspath(sourceSet.getRuntimeClasspath());

        evosuiteTest.mustRunAfter(tasks.getByName("test"));
    }
}
