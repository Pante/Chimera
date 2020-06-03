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

import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.apache.maven.plugin.logging.Log;


public class Console {
    
    public static final Console WARNINGS = new Console((log, message) -> log.warn(message), "WARNING", "warning");
    public static final Console ERRORS = new Console((log, message) -> log.error(message), "FAILURE", "error");
    
    
    BiConsumer<Log, String> consumer;
    String header;
    String count;
    
    
    Console(BiConsumer<Log, String> consumer, String header, String count) {
        this.consumer = consumer;
        this.header = header;
        this.count = count;
    }
    
    
    public void log(Log logger, List<Entry<Class<?>, String>> messages) {
        if (messages.isEmpty()) {
            return;
        }
        
        logger.info("-------------------------------------------------------------");
        consumer.accept(logger, "RESOLUTION " + header + ":");
        logger.info("-------------------------------------------------------------");
        for (var message : messages) {
            if (message.getKey() != null) {
                consumer.accept(logger, message.getKey().getName() + ": " + message.getValue());
                
            } else {
                consumer.accept(logger, message.getValue());
            }
        }
        logger.info(messages.size() + " " + (messages.size() == 1 ? count : count + "s"));
        logger.info("-------------------------------------------------------------");
    }
    
}
