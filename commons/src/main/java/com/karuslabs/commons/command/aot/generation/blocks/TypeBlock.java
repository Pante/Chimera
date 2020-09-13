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
package com.karuslabs.commons.command.aot.generation.blocks;

import java.time.LocalDateTime;


/**
 * A code generator for classes.
 */
public class TypeBlock {

    StringBuilder builder;
    
    
    /**
     * Creates a {@code TypeBlock}.
     */
    public TypeBlock() {
        builder = new StringBuilder();
    }
    
    
    /**
     * Generates the package, required imports and a public class with the given name.
     * 
     * @param pack the package which contains the generated class
     * @param name the name of the generated class
     */
    public void start(String pack, String name) {
        builder.setLength(0);
        if (!pack.isEmpty()) {
            builder.append("package ").append(pack).append(";\n\n");
        }
        builder.append("import com.karuslabs.commons.command.tree.nodes.*;\n\n");
        builder.append("import com.mojang.brigadier.tree.CommandNode;\n\n");
        builder.append("import java.util.*;\n");
        builder.append("import java.util.function.Predicate;\n\n");
        builder.append("import org.bukkit.command.CommandSender;\n\n\n");
        builder.append("/**\n");
        builder.append(" * This file was generated at ").append(LocalDateTime.now()).append(" using Chimera 4.8.0\n");
        builder.append(" */\n");
        builder.append("public class ").append(name).append(" {\n\n");
        builder.append("    private static final Predicate<CommandSender> REQUIREMENT = s -> true;\n\n");
    }
    
    
    /**
     * Adds the generated method to this generated class.
     * 
     * @param method the generated method
     */
    public void method(String method) {
        builder.append(method);
    }
    
    
    /**
     * Returns the generated class.
     * 
     * @return the generated class
     */
    public String end() {
        return builder.append("}\n").toString();
    }
    
}
