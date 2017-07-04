package com.github.cseppento.gradle.evosuite.tasks;

import com.github.cseppento.gradle.evosuite.EvoSuiteExtension;
import com.github.cseppento.gradle.evosuite.EvoSuitePlugin;
import com.github.cseppento.gradle.evosuite.internal.DefaultEvoSuiteExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskInstantiationException;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cseppento.gradle.evosuite.testhelper.ExceptionMessageMatcher.messageContainsString;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public abstract class TaskIntegTestBase<T extends JavaExec> {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    final Class<? extends T> sutClass;
    Project project;
    T sut;
    EvoSuiteExtension extension;
    Configuration config;

    public TaskIntegTestBase(Class<? extends T> sutClass) {
        this.sutClass = sutClass;
    }

    @Before
    public void setUp() throws Exception {
        project = ProjectBuilder.builder().build();
    }

    void createSut() {
        sut = project.getTasks().create("sut", sutClass);
    }

    void createExtension() {
        extension = project.getExtensions().create(
                EvoSuiteExtension.class, EvoSuitePlugin.EXTENSION_NAME, DefaultEvoSuiteExtension.class,
                project, "VER", "MAIN");
    }

    void createGenConfig() {
        config = project.getConfigurations().create(EvoSuitePlugin.TEST_GENERATION_CONFIG_NAME);
    }

    private void expectFailure(String message) {
        thrown.expect(TaskInstantiationException.class);
        thrown.expectMessage("Could not create task of type '" + sutClass.getSimpleName() + "'");
        thrown.expectCause(isA(IllegalStateException.class));
        thrown.expectCause(messageContainsString(message));
    }

    @Test
    public void testCreateFailsIfExtensionIsMissing() {
        expectFailure("The 'evosuite' extension has not been applied");
        createSut();
    }

    @Test
    public void testCreateFailsIfConfigurationIsMissing() {
        createExtension();

        expectFailure("The 'evosuiteGeneration' configuration was not found");
        createSut();
    }

    @Test
    public void testCreateDefault() throws Exception {
        createExtension();
        createGenConfig();

        createSut();

        checkCreateCommons();
        checkCreateDefault();
    }

    void checkCreateCommons() {
        assertEquals("MAIN", sut.getMain());
        assertEquals(config, sut.getClasspath());

        assertEquals(null, sut.getGroup());
        assertEquals(null, sut.getDescription());
    }

    abstract void checkCreateDefault();
}
