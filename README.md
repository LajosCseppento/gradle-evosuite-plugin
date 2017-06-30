# gradle-evosuite-plugin

[![Build Status](https://travis-ci.org/cseppento/gradle-evosuite-plugin.svg?branch=master)](https://travis-ci.org/cseppento/gradle-evosuite-plugin)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.github.cseppento:gradle-evosuite-plugin)](https://sonarcloud.io/dashboard/index/com.github.cseppento:gradle-evosuite-plugin)
[![codebeat badge](https://codebeat.co/badges/d88aada1-10c7-4532-99a4-88817caaaf6b)](https://codebeat.co/projects/github-com-cseppento-gradle-evosuite-plugin-master)

Gradle plugin for generating tests using [EvoSuite](http://www.evosuite.org/).

> EvoSuite automatically generates JUnit test suites for Java classes, targeting 
> code coverage criteria such as branch coverage. It uses an evolutionary 
> approach based on a genetic algorithm to derive test suites. To improve 
> readability, the generated unit tests are minimized, and regression assertions 
> that capture the current behavior of the tested classes are added to the tests.
> -- <cite>[EvoSuite/evosuite](https://github.com/EvoSuite/evosuite)</cite>
