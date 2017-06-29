package com.github.cseppento.gradle.evosuite

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class EvoSuiteGradlePluginFunctTest extends Specification {
    @Rule
    TemporaryFolder tmpDir = new TemporaryFolder()
    File buildFile

    def setup() {
        buildFile = tmpDir.newFile('build.gradle')
    }

    def 'set tool version'() {
        given:
        buildFile << '''
                plugins {
                    id 'java'
                    id 'com.github.cseppento.evosuite'
                }
                
                evosuite {
                    toolVersion = 'TOOL_VER'
                }
                
                println '== EvoSuite tool version: ' + evosuite.toolVersion  
                '''

        when:
        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tmpDir.root)
                .withArguments('properties')
                .build()

        then:
        assert result.output.contains('== EvoSuite tool version: TOOL_VER')
    }
}
