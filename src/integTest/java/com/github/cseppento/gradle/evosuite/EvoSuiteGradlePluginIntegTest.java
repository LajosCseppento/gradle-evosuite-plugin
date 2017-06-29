package com.github.cseppento.gradle.evosuite;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.plugins.PluginApplicationException;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.cseppento.gradle.evosuite.testhelper.ExceptionMessageMatcher.messageContainsString;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;

public class EvoSuiteGradlePluginIntegTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Project project;
    private Path projectDir;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().withName("test-project").build();
        projectDir = project.getProjectDir().toPath();
    }

    @Test
    public void testSourceSets() {
        project.getPluginManager().apply(JavaPlugin.class);
        project.getPluginManager().apply(EvoSuiteGradlePlugin.class);

        SourceSetContainer sourceSets = (SourceSetContainer) project.property("sourceSets");
        SourceSet sourceSet = sourceSets.getByName("evosuiteTest");

        assertEquals(1, sourceSet.getJava().getSrcDirs().size());
        assertEquals(1, sourceSet.getResources().getSrcDirs().size());

        Set<Path> compileCp = toRelPaths(sourceSet.getCompileClasspath().getFiles());
        Set<Path> runtimeCp = toRelPaths(sourceSet.getRuntimeClasspath().getFiles());

        assertEquals(pathSet("build/classes/java/main", "build/resources/main"), compileCp);
        assertEquals(pathSet(
                "build/classes/java/main", "build/resources/main",
                "build/classes/java/evosuiteTest", "build/resources/evosuiteTest"), runtimeCp);
    }

    @Test
    public void testApplyFailsIfJavaPluginIsNotApplied() {
        thrown.expect(PluginApplicationException.class);
        thrown.expectMessage("Failed to apply plugin");
        thrown.expectCause(isA(GradleException.class));
        thrown.expectCause(messageContainsString("Please apply the java plugin first in order to use the EvoSuite plugin"));

        project.getPluginManager().apply(EvoSuiteGradlePlugin.class);
    }

    private Set<Path> toRelPaths(Set<File> files) {
        return files.stream().map(File::toPath).map(projectDir::relativize).collect(Collectors.toSet());
    }

    private Set<Path> pathSet(String... paths) {
        return Stream.of(paths).map(Paths::get).collect(Collectors.toSet());
    }
}
