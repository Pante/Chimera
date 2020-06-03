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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.scribe.annotations.*;
import com.karuslabs.scribe.core.Environment;

import java.util.Set;


public class APIParser<T> extends SingleParser<T> {
    
    public APIParser(Environment<T> environment) {
        super(environment, Set.of(API.class), "API");
    }

    
    @Override
    protected void parse(T type) {
       var api = environment.resolver.any(type, API.class);
       if (api.value() != Version.INFERRED) {
           environment.mappings.put("api-version", api.value().toString());
           return;
       }
       
       for (var version : Version.values()) {
           if (environment.project.api.startsWith(version + ".") || environment.project.api.startsWith(version + "-")) {
               environment.mappings.put("api-version", version.toString());
               break;
           }
       }
       
       if (!environment.mappings.containsKey("api-version")) {
           environment.mappings.put("api-version", Version.INFERRED.toString());
           environment.warn(type, "Could not infer \"api-version\", \"" + Version.INFERRED + "\" will be used instead");
       }
    }

}
