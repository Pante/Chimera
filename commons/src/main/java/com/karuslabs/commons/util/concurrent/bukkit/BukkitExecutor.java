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
package com.karuslabs.commons.util.concurrent.bukkit;

import com.karuslabs.commons.util.concurrent.*;
        
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;


public abstract class BukkitExecutor {
    
    protected BukkitScheduler scheduler;
    protected Plugin plugin;
    
    
    public BukkitExecutor(Plugin plugin) {
        this.scheduler = plugin.getServer().getScheduler();
        this.plugin = plugin;
    }
    
    
    public <T> Result<T> submit(Callable<T> callable) {
        return submit(callable, 0L);
    }

    public <T> Result<T> submit(Callable<T> callable, long delay) {
        return submit(new BukkitResultTask<>(callable), delay);
    }

    
    public <T> Result<T> submit(Runnable runnable, T result) {
        return submit(runnable, result, 0L);
    }

    public <T> Result<T> submit(Runnable runnable, T result, long delay) {
        return submit(new BukkitResultTask<>(runnable, result), delay);
    }

    
    public Result<?> submit(Runnable runnable) {
        return submit(runnable, 0L);
    }

    public Result<?> submit(Runnable runnable, long delay) {
        return submit(new BukkitResultTask<>(runnable, null), delay);
    }
    
    
    protected abstract <T> Result<T> submit(BukkitResultTask<T> result, long delay);

    
    public <T> Result<T> schedule(Consumer<Callback<T>> consumer, long period) {
        return schedule(consumer, period, 0L);
    }

    public <T> Result<T> schedule(Consumer<Callback<T>> consumer, long period, long delay) {
        return schedule(ScheduledBukkitResultTask.of(consumer), period, delay);
    }

    
    public <T> Result<T> schedule(Runnable runnable, T result, long period) {
        return schedule(runnable, result, period, 0L);
    }

    public <T> Result<T> schedule(Runnable runnable, T result, long period, long delay) {
        return schedule(new ScheduledBukkitResultTask(runnable, result), period, delay);
    }

    
    public Result<?> schedule(Runnable runnable, long period) {
        return schedule(runnable, period, 0L);
    }

    public Result<?> schedule(Runnable runnable, long period, long delay) {
        return schedule(new ScheduledBukkitResultTask(runnable, null), period, delay);
    }
    
    
    protected abstract <T> Result<T> schedule(ScheduledBukkitResultTask<T> result, long period, long delay);

}

class AsyncBukkitExecutor extends BukkitExecutor {

    AsyncBukkitExecutor(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected <T> Result<T> submit(BukkitResultTask<T> result, long delay) {
        var task = scheduler.runTaskLaterAsynchronously(plugin, result, delay);
        result.setTask(task);
        return result;
    }

    @Override
    protected <T> Result<T> schedule(ScheduledBukkitResultTask<T> result, long period, long delay) {
        var task = scheduler.runTaskTimerAsynchronously(plugin, result, delay, period);
        result.setTask(task);
        return result;
    }
    
}

class SyncBukkitExecutor extends BukkitExecutor {

    SyncBukkitExecutor(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected <T> Result<T> submit(BukkitResultTask<T> result, long delay) {
        BukkitTask task = scheduler.runTaskLater(plugin, result, delay);
        result.setTask(task);
        
        return result;
    }

    @Override
    protected <T> Result<T> schedule(ScheduledBukkitResultTask<T> result, long period, long delay) {
        BukkitTask task = scheduler.runTaskTimer(plugin, result, delay, period);
        result.setTask(task);
        return result;
    }
    
}