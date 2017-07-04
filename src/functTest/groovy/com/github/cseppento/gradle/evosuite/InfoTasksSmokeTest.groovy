package com.github.cseppento.gradle.evosuite

import com.github.cseppento.gradle.evosuite.testhelper.TestProject
import groovy.io.FileType
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class InfoTasksSmokeTest extends Specification {
    @Rule
    TemporaryFolder tmpDir = new TemporaryFolder()
    File buildFile
    BuildResult result

    def setup() {
        buildFile = tmpDir.newFile('build.gradle')
        buildFile << '''
            plugins {
                id 'java'
                id 'com.github.cseppento.evosuite'
            }
            
            repositories { mavenCentral() }
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

    def 'task evosuiteHelp'() {
        when:
        execute('evosuiteHelp')

        then:
        result.task(':evosuiteHelp').outcome == TaskOutcome.SUCCESS
        result.output.contains('usage: EvoSuite')
    }

    def 'task evosuiteParameters'() {
        when:
        execute('evosuiteParameters')

        then:
        result.task(':evosuiteParameters').outcome == TaskOutcome.SUCCESS
        result.output.contains('search_budget')
        result.output.contains('Maximum search duration')
    }

    private void execute(String... args) {
        println '== Execution'
        result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tmpDir.root)
                .withArguments(args)
                .forwardOutput()
                .build()
        println ''
    }
}
