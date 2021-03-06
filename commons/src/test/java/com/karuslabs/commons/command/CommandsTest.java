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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


class CommandsTest {
    
    Literal<Object> literal = Literal.builder("name").then(Literal.builder("a").alias("a1", "a2")).then(Argument.builder("b", word())).build();
    Command<Object> execution = val -> 1;
    
    
    @Test
    void execution() {
        Commands.execution(literal, execution);
        assertSame(execution, literal.getCommand());
    }
    
    
    @ParameterizedTest
    @MethodSource("remove_parameters")
    void remove(String child, boolean removed, int size) {
        assertEquals(4, literal.getChildren().size());
        
        assertEquals(removed, Commands.remove(literal, child) != null);
        assertEquals(size, literal.getChildren().size());
    }
    
    static Stream<Arguments> remove_parameters() {
        return Stream.of(
            of("a", true, 1),
            of("c", false, 4)
        );
    }
    
} 
