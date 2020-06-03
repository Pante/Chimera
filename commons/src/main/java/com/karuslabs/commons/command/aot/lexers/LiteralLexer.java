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
package com.karuslabs.commons.command.aot.lexers;

import com.karuslabs.commons.command.aot.*;

import java.util.*;
import javax.lang.model.element.Element;

import static com.karuslabs.annotations.processor.Messages.*;
import static java.util.Collections.EMPTY_LIST;


/**
 * A {@code Lexer} subclass that produces a single literal token from a string.
 * Literals should match {@code (name)(|(alias))*}.
 */
public class LiteralLexer implements Lexer {
    
    /**
     * Produces a single literal token from {@code raw}.
     * 
     * @param environment the environment
     * @param location the location that {@code raw} was declared
     * @param raw the value
     * @return a single literal token if {@code raw} is valid; else an empty list
     */
    @Override
    public List<Token> lex(Environment environment, Element location, String raw) {
        if (raw.contains("<") || raw.contains(">")) {
            environment.error(location, format(raw, "contains \"<\"s and \">\"s", "a literal should not contain \"<\"s and \">\"s"));
            return EMPTY_LIST;
        }
        
        var names = raw.split("\\|", -1);
        
        for (var name : names) {
            if (!valid(environment, location, raw, name)) {
                return EMPTY_LIST;
            }
        }
        
        var aliases = new HashSet<String>();
        for (int i = 1; i < names.length; i++) {
            var alias = names[i];
            if (!aliases.add(alias)) {
                environment.warn(location, "Duplicate alias: " + quote(alias));
            }
        }
        
        return List.of(Token.literal(location, raw, names[0], aliases));
    }
    
    
    /**
     * Checks if the given value is valid.
     * 
     * @param environment the environment
     * @param location the location that {@code raw} was declared
     * @param context the context
     * @param value the value
     * @return {@code true} if valid; else {@code false}
     */
    boolean valid(Environment environment, Element location, String context, String value) {
        var error = value.isEmpty();
        if (error) {
            environment.error(location, format(context, "contains an empty literal alias or name", "should not be empty"));
        }
        
        return !error;
    }

}
