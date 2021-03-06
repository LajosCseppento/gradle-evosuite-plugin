package com.github.cseppento.gradle.evosuite.testhelper;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 * The purpose of this class is to parse and copy test projects (which are inputs for test cases).
 */
public class TestProject {
    private final Path projectDir;

    public TestProject(String projectName) throws IOException {
        Objects.requireNonNull(projectName);
        this.projectDir = getProjectDirFor(projectName);
    }

    private static Path getProjectDirFor(String projectName) throws IOException {
        try {
            // used when the classpath is properly configured (Gradle)
            URL res = Resources.getResource(projectName);

            if (!res.getProtocol().equals("file")) {
                throw new IOException("Only file resources supported for testing: " + res);
            }

            return Paths.get(res.toURI());
        } catch (Exception ex) {
            // used when test was started inside an IDE
            Path dir = Paths.get("test-projects/simple").toAbsolutePath();

            if (dir.toFile().isDirectory()) {
                return dir;
            } else {
                String msg = String.format("Could not get project dir for '%s'. %s is not a directory and %s",
                        projectName, dir, ex.getMessage());
                throw new IOException(msg);
            }
        }
    }

    public void copyWithGeneratedTests(Path target) throws IOException {
        copy(target, true);
    }

    public void copyWithoutGeneratedTests(Path target) throws IOException {
        copy(target, false);
    }

    private void copy(Path target, boolean includeGeneratedTests) throws IOException {
        Objects.requireNonNull(target);
        File targetAsFile = target.toFile();
        Preconditions.checkArgument(!targetAsFile.exists() || targetAsFile.isDirectory(),
                "The target already exists and it is not a directory: %s", target);

        Files.walkFileTree(projectDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                String name = dir.getFileName().toString();
                if (name.startsWith(".") || name.equals("bin") || name.equals("build") || name.equals("out")) {
                    // ignore hidden and build dirs
                    return FileVisitResult.SKIP_SUBTREE;
                } else if (!includeGeneratedTests && (name.equals("evoTest") || name.equals("evosuite-report"))) {
                    // ignore formerly generated tests if present
                    return FileVisitResult.SKIP_SUBTREE;
                } else {
                    Files.createDirectories(target.resolve(projectDir.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String name = file.getFileName().toString();
                if (name.startsWith(".") || name.endsWith(".iml") || name.endsWith(".ipr") || name.endsWith(".iws")) {
                    // ignore hidden and IDE configs
                    return FileVisitResult.CONTINUE;
                } else {
                    Files.copy(file, target.resolve(projectDir.relativize(file)));
                    return FileVisitResult.CONTINUE;
                }
            }
        });
    }
}
