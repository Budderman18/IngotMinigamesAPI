package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * This class handles everything around timers. 
 * Useful for determine when events should be run. 
 * 
 */
public class TimerHandler {
    //global vars
    private static final BukkitScheduler schedule = Bukkit.getServer().getScheduler();
    /**
     * 
     * This constructor blocks new() usage as it does nothing
     * 
     */
    private TimerHandler() {}
    /**
     *
     * This method creates a new Timer instance.You can give it a name, time (in seconds), 
     * weather or not it should start running and weather or not it increases or decreases. 
     *
     * @param pluginn The plugin to attach to
     * @param startTimee The time to start at
     * @param endTimee The time to end at
     * @param actionn The runnable to execute when finished
     * @param async weather or not to run asyncronisouly (DO NOT USE IF YOU NEED BUKKIT USAGE)
     * @param useTicks true to use ticks rather than seconds
     * @return the task number
     */
    public static int runTimer(Plugin pluginn, long startTimee, long endTimee, Runnable actionn, boolean useTicks, boolean async) {
        //local vars
        int taskNumber = 0;
        byte multiplier = 20;
        //create endVar
        long endVar = startTimee - endTimee;
        //absolute value of endVar
        if (endVar < 0) {
            endVar *= -1;
        }
        //check if using ticks
        if (useTicks == true) {
            multiplier = 1;
        }
        //check if not using async
        if (async == false && actionn != null) {
            //run timer
            taskNumber = schedule.scheduleSyncDelayedTask(pluginn, () -> {
                actionn.run();
            }, endVar * multiplier);
        }
        else if (actionn != null) {
            //run async timer
            taskNumber = schedule.scheduleAsyncDelayedTask(pluginn, () -> {
                actionn.run();
            }, endVar * multiplier); 
        }
        return taskNumber;
    }
    /**
     * 
     * This method cancels a given timer. 
     * 
     * @param taskNumberr The task number to cancel
     */
    public static void cancelTimer(int taskNumberr) {
        schedule.cancelTask(taskNumberr);
    }
    /**
     * 
     * This method cancels all timers of the given plugin 
     * Not recommended as it will likely break things
     * 
     * @param plugin The plugin to cancel tasks for
     */
    @Deprecated
    public static void cancelAllTimers(Plugin plugin) {
        schedule.cancelTasks(plugin);
    }
    /**
     *
     * This method checks if the timer is currently running. 
     *
     * @param taskNumberr the task number to check
     * @return true if running
     */
    public boolean checkIfRunning(int taskNumberr) {
        return schedule.isCurrentlyRunning(taskNumberr);
    }
}
