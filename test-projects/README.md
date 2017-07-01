# gradle-evosuite-plugin / test-projects

These projects serve as test inputs for `gradle-evosuite-plugin` test-projects which  
intentionally contain bugs and sometimes expect unrealistic interactions to happen. 
These projects also contain both simple classes and applications in order to test
`gradle-evosuite-plugin` with more realistic projects.

`simple` contains a class with a static method, an object with internal state 
and an application which requires interaction through standard I/O and has timing delays. 
The project has no compile dependencies.

`duckduckgo` provides classes for a simple communication with the 
[DuckDuckGo Instant Answer API](https://api.duckduckgo.com/api) and an application which 
sends search requests to this API specified by the user. This project depends on Apache 
HttpClient, Jackson Databind, SLF4J and Logback.
