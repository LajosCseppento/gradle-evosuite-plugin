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

class TestExecutionSmokeTest extends Specification {
    @Rule
    TemporaryFolder tmpDir = new TemporaryFolder()
    File buildFile
    BuildResult result

    def setup() {
        new TestProject('simple').copyWithGeneratedTests(tmpDir.root.toPath())

        buildFile = tmpDir.newFile('build.gradle')
        buildFile << '''
            plugins {
                id 'java'
                id 'com.github.cseppento.evosuite'
            }
            
            repositories { mavenCentral() }
            
            tasks.withType(Test) {
                testLogging {
                    exceptionFormat = 'full'
                }
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

    def 'execute tests with defaults'() {
        when:
        executeTests()

        then:
        checkResult()
    }

    def 'execute tests with 1.0.4'() {
        given:
        buildFile << '''
            evosuite {
                toolVersion = '1.0.4'
            }
            '''

        when:
        executeTests()

        then:
        checkResult()
    }

    private void executeTests() {
        println '== Execution'
        result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tmpDir.root)
                .withArguments('evosuiteTest')
                .forwardOutput()
                .build()
        println ''
    }

    private void checkResult(String toolVersion) {
        def testClasses = ['SimpleApp', 'SimpleObjectSut', 'SimpleStaticSut'].collect {
            'com.github.cseppento.gradle.evosuite.testprojects.simple.' + it + '_ESTest'
        }

        // check JUnit result files
        Path testResultDir = tmpDir.root.toPath().resolve('build/test-results/evosuiteTest');

        assert Files.isDirectory(testResultDir)
        assert Files.isDirectory(testResultDir.resolve('binary'))
        assert Files.isRegularFile(testResultDir.resolve('binary/output.bin'))
        assert Files.isRegularFile(testResultDir.resolve('binary/output.bin.idx'))
        assert Files.isRegularFile(testResultDir.resolve('binary/results.bin'))
        testClasses.each { testCls ->
            assert Files.isRegularFile(testResultDir.resolve('TEST-' + testCls + '.xml'))
        }

        // check JUnit report files
        Path testReportDir = tmpDir.root.toPath().resolve('build/reports/tests/evosuiteTest');
        assert Files.isDirectory(testReportDir)
        assert Files.isReadable(testReportDir.resolve('index.html'))
        testClasses.each { testCls ->
            assert Files.isRegularFile(testReportDir.resolve('classes/' + testCls + '.html'))
        }
    }
}
