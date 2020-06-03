/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.scribe.maven.plugin;

import com.karuslabs.scribe.core.*;

import io.github.classgraph.ClassGraph;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import org.apache.maven.model.Contributor;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import static java.util.stream.Collectors.toList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;


/**
 * A MOJO that represents the {@code scribe} goal.
 */
@Mojo(name = "scribe", defaultPhase = COMPILE, threadSafe = false, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ScribeMojo extends AbstractMojo {    
    
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject pom;
    
    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
    List<String> classpaths;
    
    @Parameter(defaultValue = "${project.basedir}/src/main/resources")
    File folder;
    

    /**
     * Processes all annotations in a project and emits a {code plugin.yml}.
     * 
     * @throws MojoFailureException if an error occurs during annotation resolution
     */
    @Override
    public void execute() throws MojoFailureException {
        var graph = new ClassGraph().enableClassInfo().enableAnnotationInfo().addClassLoader(Processor.loader(classpaths));
        var environment = new MavenEnvironment(project());
        
        try (var processor = new MavenProcessor(environment, graph)) {   
            processor.run();
            
            if (valid(environment)) {
                var yaml = YAML.fromFile("Scribe Maven Plugin", new File(folder, "plugin.yml"));
                yaml.write(environment.mappings);

            } else {
                throw new MojoFailureException("Could not resolve annotations");
            }
        }
    }
    
    /**
     * Creates a {@code Project} from this project's {@code pom.xml}.
     * 
     * @return a {@code Project}
     */
    Project project() {
        var authors = Stream.of(pom.getContributors(), pom.getDevelopers())
                            .flatMap(Collection::stream)
                            .map(Contributor::getName)
                            .collect(toList());
        
        var api = "";
        for (var dependency : pom.getDependencies()) {
            var group = Project.DEPENDENCIES.get(dependency.getArtifactId());
            if (Objects.equals(group, dependency.getGroupId())) {
                api = dependency.getVersion();
                break;
            }
        }
        return new Project(pom.getName(), pom.getVersion(), api, authors, pom.getDescription(), pom.getUrl());
    }
    
    
    /**
     * Checks if the given environment contains any errors and warnings. If so,
     * log the errors and warnings to console.
     * 
     * @param environment the environment
     * @return {@code true} if the given environment contains errors; else {@code false}
     */
    boolean valid(MavenEnvironment environment) {
        Console.WARNINGS.log(getLog(), environment.warnings);
        Console.ERRORS.log(getLog(), environment.errors);
        
        return environment.errors.isEmpty();
    }
    
}
