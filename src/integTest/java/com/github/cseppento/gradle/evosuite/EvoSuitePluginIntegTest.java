package com.github.cseppento.gradle.evosuite;

import com.google.common.collect.Sets;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.cseppento.gradle.evosuite.testhelper.ExceptionMessageMatcher.messageContainsString;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;

public class EvoSuitePluginIntegTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Project project;
    private Path projectDir;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().withName("test-project").build();
        project.getRepositories().mavenCentral();
        projectDir = project.getProjectDir().toPath();
    }

    @Test
    public void testSourceSets() {
        project.getPluginManager().apply(JavaPlugin.class);
        project.getPluginManager().apply(EvoSuitePlugin.class);

        SourceSetContainer sourceSets = (SourceSetContainer) project.property("sourceSets");
        SourceSet sourceSet = sourceSets.getByName("evosuiteTest");

        assertEquals(1, sourceSet.getJava().getSrcDirs().size());
        assertEquals(1, sourceSet.getResources().getSrcDirs().size());

        Set<Path> srcDirs = toRelPaths(sourceSet.getAllSource().getSrcDirs());
        assertEquals(pathSet("src/evoTest/java", "src/evoTest/resources"), srcDirs);

        Set<Path> compileCp = toRelPaths(sourceSet.getCompileClasspath().getFiles());
        Set<Path> runtimeCp = toRelPaths(sourceSet.getRuntimeClasspath().getFiles());

        assertThat(compileCp, hasItems(pathArray("build/classes/java/main", "build/resources/main")));
        assertThat(runtimeCp, hasItems(pathArray(
                "build/classes/java/main", "build/resources/main",
                "build/classes/java/evosuiteTest", "build/resources/evosuiteTest")));

        Optional<Path> evoSuiteDep = compileCp.stream()
                .filter(p -> p.getFileName().endsWith("evosuite-client-1.0.5.jar"))
                .findAny();
        assertTrue("Missing EvoSuite dependency: " + compileCp, evoSuiteDep.isPresent());

        assertEquals(pathSet("build/classes/java/evosuiteTest", "build/resources/evosuiteTest"), Sets.difference(runtimeCp, compileCp));
    }

    @Test
    public void testApplyFailsIfJavaPluginIsNotApplied() {
        thrown.expect(PluginApplicationException.class);
        thrown.expectMessage("Failed to apply plugin");
        thrown.expectCause(isA(GradleException.class));
        thrown.expectCause(messageContainsString("Please apply the java plugin first in order to use the EvoSuite plugin"));

        project.getPluginManager().apply(EvoSuitePlugin.class);
    }

    private Set<Path> toRelPaths(Set<File> files) {
        return files.stream()
                .map(File::toPath)
                .map(projectDir::relativize)
                .collect(Collectors.toSet());
    }

    private Set<Path> pathSet(String... paths) {
        return Stream.of(paths).map(Paths::get).collect(Collectors.toSet());
    }

    private Path[] pathArray(String... paths) {
        return Stream.of(paths).map(Paths::get).toArray(Path[]::new);
    }
}
