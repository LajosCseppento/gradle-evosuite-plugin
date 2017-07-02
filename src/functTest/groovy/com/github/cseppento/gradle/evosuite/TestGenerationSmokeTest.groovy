package com.github.cseppento.gradle.evosuite

import com.github.cseppento.gradle.evosuite.testhelper.TestProject
import groovy.io.FileType
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class TestGenerationSmokeTest extends Specification {
    @Rule
    TemporaryFolder tmpDir = new TemporaryFolder()
    File buildFile
    BuildResult result

    def setup() {
        new TestProject('simple').copyWithoutGeneratedTests(tmpDir.root.toPath())

        buildFile = tmpDir.newFile('build.gradle')
        buildFile << '''
            plugins {
                id 'java'
                id 'com.github.cseppento.evosuite'
            }
            
            repositories { mavenCentral() }
            
            tasks.evosuiteGenerateTests {
                args '-Dsearch_budget=3', '-Dstopping_condition=MaxTime',
                     '-Dminimization_timeout=0', '-Djunit_check_timeout=0', '-Dassertion_timeout=0'
            }
            '''

        println '== Test case: ' + this.specificationContext.currentIteration.name
        println '== Files before execution:'
        tmpDir.root.eachFileRecurse(FileType.FILES) { println it }
        println ''
    }

    void cleanup() {
        println '== Files after execution:'
        tmpDir.root.eachFileRecurse(FileType.FILES) { println it }
        println ''
    }

    def 'generate tests with defaults'() {
        when:
        generateTests()

        then:
        checkResult( '1.0.5')
    }

    def 'generate tests with 1.0.4'() {
        given:
        buildFile << '''
            evosuite {
                toolVersion = '1.0.4'
            }
            '''

        when:
        generateTests()

        then:
        checkResult( '1.0.4')
    }

    private void generateTests() {
        println '== Execution'
        result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tmpDir.root)
                .withArguments('evosuiteGenerateTests')
                .forwardOutput()
                .build()
        println ''
    }

    private void checkResult(String toolVersion) {
        def classes = ['SimpleApp', 'SimpleObjectSut', 'SimpleStaticSut'].collect {
            'com.github.cseppento.gradle.evosuite.testprojects.simple.' + it
        }

        assert result.output.contains('EvoSuite ' + toolVersion)
        assert result.output.contains('Computation finished')

        Path testTargetDir = tmpDir.root.toPath().resolve('src/evoTest/java')
        assert Files.isDirectory(testTargetDir)

        classes.each { cls ->
            String fileNameBase = cls.replace('.', '/') + '_ESTest'
            assert Files.isRegularFile(testTargetDir.resolve(fileNameBase + '.java'))
            assert Files.isRegularFile(testTargetDir.resolve(fileNameBase + '_scaffolding.java'))
        }
        assert new File(tmpDir.root, 'evosuite-report/statistics.csv').exists()
    }
}
