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
package com.karuslabs.commons.item.builders;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.meta.*;


/**
 * A banner builder.
 */
public class BannerBuilder extends Builder<BannerMeta, BannerBuilder> {
    
    /**
     * Creates a {@code BannerBuilder} for the given material.
     * 
     * @param material the material
     * @return a {@code BannerBuilder}
     */
    public static BannerBuilder of(Material material) {
        return new BannerBuilder(material);
    }
    
    BannerBuilder(Material material) {
        super(material);
    }
    
    BannerBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    /**
     * Adds the given patterns.
     * 
     * @param patterns the patterns
     * @return {@code this}
     */
    public BannerBuilder patterns(Collection<Pattern> patterns) {
        return patterns(patterns.toArray(new Pattern[0]));
    }
    
    /**
     * Adds the given patterns.
     * 
     * @param patterns the patterns
     * @return {@code this}
     */
    public BannerBuilder patterns(Pattern... patterns) {
        for (var pattern : patterns) {
            meta.addPattern(pattern);
        }
        return this;
    }
    
    
    @Override
    BannerBuilder self() {
        return this;
    }
    
}
