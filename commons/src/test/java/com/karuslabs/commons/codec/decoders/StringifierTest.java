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

package com.karuslabs.commons.codec.decoders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class StringifierTest {
    
    static final String ENCODED = "codec/decoder/encoded.";
    
    
    @Test
    void visit_json() {
        var results = Stringifier.stringify().from(getClass().getClassLoader().getResourceAsStream(ENCODED + "json"), "json");
        assertEquals(9, results.size());
        
        assertEquals("first", results.get("a.b[0]"));
        assertEquals("second", results.get("a.b[1].c[0]"));
        assertEquals("value", results.get("a.b[2][0]"));
        assertEquals("1", results.get("a.b[1].c[1]"));
        assertEquals("false", results.get("a.b[1].c[2]"));
        assertEquals("third", results.get("e.f"));
        assertEquals("fourth", results.get("g"));
    }
    
    
    @Test
    void visit_properties() {
        var results = Stringifier.stringify().from(getClass().getClassLoader().getResourceAsStream(ENCODED + "properties"), "properties");
        assertEquals(2, results.size());
        
        assertEquals("value", results.get("a"));
        assertEquals("true", results.get("b"));
    }
    
    
    @Test
    void visit_yaml() {
        var results = Stringifier.stringify().from(getClass().getClassLoader().getResourceAsStream(ENCODED + "yml"), "yml");
        assertEquals(6, results.size());
        
        assertEquals("first", results.get("a.b[0]"));
        assertEquals("1", results.get("a.b[1]"));
        assertEquals("true", results.get("a.b[2]"));
        
        assertEquals("second", results.get("c.d"));
        
        assertEquals("third", results.get("e"));
        
        assertEquals("", results.get("f"));
    }
    
}