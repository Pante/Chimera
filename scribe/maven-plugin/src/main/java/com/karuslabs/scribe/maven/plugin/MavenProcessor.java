/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
import com.karuslabs.scribe.core.parsers.PluginParser;

import io.github.classgraph.*;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {@code Processor} for Maven projects.
 */
public class MavenProcessor extends Processor<Class<?>> implements AutoCloseable {
    
    ClassGraph graph;
    @Nullable ScanResult results;
    
    
    /**
     * Creates a {@code MavenProcessor} with the given parameters.
     * 
     * @param environment the environment
     * @param graph a graph
     */
    public MavenProcessor(Environment<Class<?>> environment, ClassGraph graph) {
        super(environment, PluginParser.type(environment));
        this.graph = graph;
    }
    
    
    /**
     * Returns all classes annotated with the given annotation.
     * 
     * @param annotation the annotation
     * @return all classes with the given annotation
     */
    @Override
    protected Stream<Class<?>> annotated(Class<? extends Annotation> annotation) {
        if (results == null) {
            results = graph.scan();
        }
        
        return results.getClassesWithAnnotation(annotation.getName()).stream().map(ClassInfo::loadClass);
    }

    
    @Override
    public void close() {
        if (results != null) {
            results.close();
        }
    }

}
