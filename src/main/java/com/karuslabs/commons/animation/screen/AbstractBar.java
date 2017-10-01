/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.animation.screen;

import com.karuslabs.commons.locale.Translation;
import com.karuslabs.commons.util.Template;

import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;


public abstract class AbstractBar extends Bar {

    protected Template<BossBar> template;
    
    
    public AbstractBar(Plugin plugin, Template<BossBar> template, Translation translation, long iterations, long delay, long period) {
        super(plugin, translation, iterations, delay, period);
        this.template = template;
    }
    
    
    public static abstract class AbstractBuilder<GenericBuilder extends AbstractBuilder, Bar extends AbstractBar> extends Builder<GenericBuilder, Bar> {
        
        public AbstractBuilder(Bar bar) {
            super(bar);
        }
        
        
        public GenericBuilder template(Template<BossBar> template) {
            bar.template = template;
            return getThis();
        }
        
    }
    
}
