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
package com.karuslabs.commons.command.tree;

import com.karuslabs.commons.command.*;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Argument<S, T> extends ArgumentCommandNode<S, T> implements Aliasable<S>, Mutable<S> {

    private CommandNode<S> destination;
    private List<CommandNode<S>> aliases;
    
    
    public Argument(String name, ArgumentType<T> type, Command<S> command, Predicate<S> requirement, SuggestionProvider<S> suggestions) {
        this(name, type, command, requirement, null, null, false, suggestions);
    }
    
    public Argument(String name, ArgumentType<T> type, Command<S> command, Predicate<S> requirement, @Nullable CommandNode<S> destination, RedirectModifier<S> modifier, boolean fork, SuggestionProvider<S> suggestions) {
        this(name, type, new ArrayList<>(0), command, requirement, destination, modifier, fork, suggestions);
    }
    
    public Argument(String name, ArgumentType<T> type, List<CommandNode<S>> aliases, Command<S> command, Predicate<S> requirement, @Nullable CommandNode<S> destination, RedirectModifier<S> modifier, boolean fork, SuggestionProvider<S> suggestions) {
        super(name, type, command, requirement, destination, modifier, fork, suggestions);
        this.destination = destination;
        this.aliases = aliases;
    }
    
    
    @Override
    public void addChild(CommandNode<S> child) {
        super.addChild(child);
        for (var alias : aliases) {
            alias.addChild(alias);
        }
    }
    
    @Override
    public CommandNode<S> removeChild(String child) {
        var removed = Commands.remove(this, child);
        for (var alias : aliases) {
            Commands.remove(alias, child);
        }
        
        return removed;
    }
        
    
    @Override
    public List<CommandNode<S>> aliases() {
        return aliases;
    }
    
    
    @Override
    public void setCommand(Command<S> command) {
        Commands.set(this, command);
        for (var alias : aliases) {
            Commands.set(alias, command);
        }
    }
    
    
    @Override
    public @Nullable CommandNode<S> getRedirect() {
        return destination;
    }

    @Override
    public void setRedirect(CommandNode<S> destination) {
        this.destination = destination;
        for (var alias : aliases) {
            if (alias instanceof Mutable<?>) {
                (((Mutable<S>) alias)).setRedirect(destination);
            }
        }
    }
    
    
    public static <S, T> Builder<S, T> of(String name, ArgumentType<T> type) {
        return new Builder<>(name, type);
    }
    
    
    public static class Builder<S, T> extends ArgumentBuilder<S, Builder<S, T>> {
        
        String name;
        ArgumentType<T> type;
        List<String> aliases;
        @Nullable SuggestionProvider<S> suggestions;
        
        
        protected Builder(String name, ArgumentType<T> type) {
            this.name = name;
            this.type = type;
            this.aliases = new ArrayList<>(0);
        }
        
        
        public Builder<S, T> alias(String alias) {
            aliases.add(alias);
            return this;
        }
        
        public Builder<S, T> executes(SingleCommand<S> command) {
            return executes((Command<S>) command);
        }
        
        public Builder<S, T> suggests(SuggestionProvider<S> suggestions) {
            this.suggestions = suggestions;
            return getThis();
        }
        
        
        @Override
        protected Builder<S, T> getThis() {
            return this;
        }

        @Override
        public Argument<S, T> build() {
            var argument = new Argument<>(name, type, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), suggestions);
            for (var child : getArguments()) {
                argument.addChild(child);
            }
            
            for (var alias : aliases) {
                Commands.alias(argument, alias);
            }
            
            return argument;
        }

    }
    
}
