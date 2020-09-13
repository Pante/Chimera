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
package com.karuslabs.commons.command.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.*;

import java.util.concurrent.CompletableFuture;

import net.minecraft.server.v1_16_R2.*;

import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * A Cartesian type.
 * 
 * @param <T> the type of the argument
 */
interface CartesianType<T> extends Type<T> {

    static final String[] EMPTY = new String[0];

    
    /**
     * Splits the remaining string of the builder by a whitespace and forwards 
     * providing suggestions to {@link #suggest(SuggestionsBuilder, Location, String[])} 
     * if the source is a player and the block the player is looking at is within
     * a 5 block radius. After which, forwards to {@link #suggest(SuggestionsBuilder, String[])}.
     * 
     * @param <S> the type of the source
     * @param context the context
     * @param builder the builder
     * @return the suggestions
     */
    @Override
    default <S> CompletableFuture<Suggestions> listSuggestions(S source, CommandContext<S> context, SuggestionsBuilder builder) {
        var remaining = builder.getRemaining();
        var parts = remaining.isBlank() ? EMPTY : remaining.split(" ");
        
        if (source instanceof Player) {
            var block = ((Player) source).getTargetBlockExact(5);
            if (block != null) {
                suggest(builder, parts, block.getLocation());
            }
        }
        
        suggest(builder, parts);
        
        return builder.buildFuture();
    }
    
    /**
     * Provides suggestions using the given parameters.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Does nothing.
     * 
     * @param builder the builder
     * @param parts the parts of the argument split by a whitespace
     * @param location the location of the block that the source is looking at if 
     *                 within a 5 block radius
     */
    default void suggest(SuggestionsBuilder builder, String[] parts, Location location) {}
    
    /**
     * Provides suggestions using the given parameters.
     * <br><br>
     * <b>Default implementation:</b><br>
     * Does nothing.
     * 
     * @param builder the builder
     * @param parts the parts of the argument split by a whitespace
     */
    default void suggest(SuggestionsBuilder builder, String[] parts) {}
    
}


/**
 * A 2D Cartesian type.
 * 
 * @param <T> the type of the argument
 */
interface Cartesian2DType<T> extends CartesianType<T> {
    
    static final ArgumentVec2 VECTOR_2D = new ArgumentVec2(true);
    
    
    @Override
    default void suggest(SuggestionsBuilder builder, String[] parts, Location location) {
        switch (parts.length) {
            case 0:
                builder.suggest(String.valueOf(location.getX()));
                builder.suggest(location.getX() + " " + location.getZ());
                break;
                
            case 1:
                builder.suggest(parts[0] + " " + location.getZ());
                break;
                
            default: // Does nothing
        }
    }

    @Override
    default ArgumentType<?> mapped() {
        return VECTOR_2D;
    }
    
}


/**
 * A 3D Cartesian type.
 * 
 * @param <T> the type of the argument
 */
interface Cartesian3DType<T> extends CartesianType<T> {
    
    static final ArgumentVec3 VECTOR_3D = new ArgumentVec3(false);
    
    
    @Override
    default void suggest(SuggestionsBuilder builder, String[] parts, Location location) {
        switch (parts.length) {
            case 0:
                builder.suggest(String.valueOf(location.getX()));
                builder.suggest(location.getX() + " " + location.getY());
                builder.suggest(location.getX() + " " + location.getY() + " " + location.getZ());
                break;
        
            case 1:
                builder.suggest(parts[0] + " " + location.getY());
                builder.suggest(parts[0] + " " + location.getY() + " " + location.getZ());
                break;
            
            case 2:
                builder.suggest(parts[0] + " " + parts[1] + " " + location.getZ());
                break;
                
            default: // Does nothing
        }
    }
    
    @Override
    default ArgumentType<?> mapped() {
        return VECTOR_3D;
    }
    
}
