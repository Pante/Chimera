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
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.BookMeta.Generation;

import org.checkerframework.checker.nullness.qual.Nullable;


public class BookBuilder extends Builder<BookMeta, BookBuilder> {
    
    public static BookBuilder of(Material material) {
        return new BookBuilder(material);
    }
    
    BookBuilder(Material material) {
        super(material);
    }
    
    BookBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    public BookBuilder author(@Nullable String name) {
        meta.setAuthor(name);
        return this;
    }
    
    public BookBuilder generation(@Nullable Generation generation) {
        meta.setGeneration(generation);
        return this;
    }
    
    
    public BookBuilder pages(Collection<String> pages) {
        return pages(pages.toArray(new String[0]));
    }
    
    public BookBuilder pages(String... pages) {
        meta.addPage(pages);
        return this;
    }
    
    
    public BookBuilder title(@Nullable String title) {
        meta.setTitle(title);
        return this;
    }
    

    @Override
    BookBuilder self() {
        return this;
    }
    
}
