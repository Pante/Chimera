/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.*;
import com.karuslabs.commons.command.completion.*;


public class LiteralProcessor implements Processor {

    @Override
    public void process(Command command, CommandExecutor executor) {
        Literals literals = executor.getClass().getAnnotation(Literals.class);
        if (literals != null) {
            for (Literal literal : literals.value()) {
                process(command, executor, literal);
            }
            
        } else {
            process(command, executor, executor.getClass().getAnnotation(Literal.class));
        }
    }
    
    protected void process(Command command, CommandExecutor executor, Literal literal) {
        Completion completion = new CachedCompletion(literal.completions());
        command.getCompletions().put(literal.index(), completion);
    }

    @Override
    public boolean hasAnnotations(CommandExecutor executor) {
        Class<? extends CommandExecutor> type = executor.getClass();
        return type.getAnnotation(Literal.class) != null || type.getAnnotation(Literals.class) != null;
    }
    
}
