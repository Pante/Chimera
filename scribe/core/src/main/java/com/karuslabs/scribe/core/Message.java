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
package com.karuslabs.scribe.core;

import com.karuslabs.annotations.ValueType;

import org.checkerframework.checker.nullness.qual.Nullable;


public @ValueType class Message<T> {

    public static enum Type {
        INFO, WARNING, ERROR
    }
    
    
    public final @Nullable T location;
    public final String value;
    public final Type type;
    
    
    public static <T> Message<T> error(T location, String value) {
        return new Message(location, value, Type.ERROR);
    }
    
    public static <T> Message<T> warning(T location, String value) {
        return new Message(location, value, Type.WARNING);
    }
    
    public static <T> Message<T> info(T location, String value) {
        return new Message(location, value, Type.INFO);
    }
    
    
    public Message(String value, Type type) {
        this(null, value, type);
    }
    
    Message(T location, String value, Type type) {
        this.location = location;
        this.value = value;
        this.type = type;
    }
    
}
