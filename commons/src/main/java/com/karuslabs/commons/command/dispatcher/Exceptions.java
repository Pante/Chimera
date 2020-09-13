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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.annotations.Static;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_16_R2.*;

import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.command.*;
import org.bukkit.craftbukkit.v1_16_R2.entity.*;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;


/**
 * This class consists exclusively of static methods that handle exceptions which
 * occur when parsing and executing commands.
 * <br><br>
 * To be honest, I copied the methods in Spigot and I have absolutely no clue what 
 * the methods do.
 * <br><br>
 * <b>Implementation details:</b><br>
 * This class was adapted from Spigot's {@code CommandDispatcher}.
 */
@Static class Exceptions {
    
    private static final Object[] EMPTY = new Object[0];
    
    
    /**
     * Reports the given exception to the {@code sender}.
     * 
     * Source: net.minecraft.server.CommandDispatcher #line: 188
     * 
     * @param sender the sender
     * @param exception the exception
     */
    static void report(CommandSender sender, CommandSyntaxException exception) {
        var listener = from(sender);
        
        listener.sendFailureMessage(ChatComponentUtils.a(exception.getRawMessage()));
        
        var input = exception.getInput();
        if (input != null && exception.getCursor() >= 0) {
            var index = Math.min(input.length(), exception.getCursor());

            var text = (new ChatComponentText("")).a(EnumChatFormat.GRAY).format(modifier -> 
                modifier.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, input))
            );
            
            if (index > 10) {
                text.c("...");
            }

            text.c(input.substring(Math.max(0, index - 10), index));
            
            if (index < input.length()) {
                var error = new ChatComponentText(input.substring(index)).a(new EnumChatFormat[]{EnumChatFormat.RED, EnumChatFormat.UNDERLINE});
                text.addSibling(error);
            }
            
            var context = new ChatMessage("command.context.here").a(new EnumChatFormat[]{EnumChatFormat.RED, EnumChatFormat.ITALIC});
            text.addSibling(context);
            
            listener.sendFailureMessage(text);
        }
    }
    
    
    /**
     * Reports the given exception to the {@code sender}.
     * 
     * Source: net.minecraft.server.CommandDispatcher #line: 213
     * 
     * @param sender the sender
     * @param exception the exception
     */
    static void report(CommandSender sender, Exception exception) {
        var listener = from(sender);
        
        var message = exception.getMessage();
        var details = new ChatComponentText(message == null ? exception.getClass().getName() : message);        
        
        // We send the stacktrace regardless of whether debug is enabled since we
        // cannot access the CommandDispatcher's logger.
        var stacktrace = exception.getStackTrace();
        for (int i = 0; i < Math.min(stacktrace.length, 3); i++) {
            var element = stacktrace[i];
            details.c("\n\n").c(element.getMethodName()).c("\n ").c(element.getFileName()).c(":").c(String.valueOf(element.getLineNumber()));
        }
                
        var failure = new ChatMessage("command.failed").format(modifier -> 
            modifier.setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, details))
        );
        listener.sendFailureMessage(failure);
        
        if (SharedConstants.d) {
            listener.sendFailureMessage(new ChatComponentText(SystemUtils.d(exception)));
            // We do not log the error since we cannot access the logger
        }
    }
    
    
    /**
     * Transforms the given {@code CommandSneder} to a {@code CommandListenerWrapper}.
     * 
     * Source: package org.bukkit.craftbukkit.command.VanillaCommandWrapper#getListener(CommandSender)
     * 
     * @param sender the sender
     * @return a {@code CommandListenerWrapper}
     */
    static CommandListenerWrapper from(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle().getCommandListener();
            
        } else if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getWrapper();
            
        } else if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock().getWrapper();
            
        } else if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.getServer()).remoteControlCommandListener.getWrapper();
            
        } else if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer().getServerCommandListener();
            
        } else if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
            
        } else {
            throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
        }
    }
    
}
