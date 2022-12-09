package com.budderman18.IngotMinigamesAPI.Core.Data;

import org.bukkit.ChatColor;

/**
 *
 * This class handles the given statuses arenas can be given
 * Used to help determine who can join and when
 * Due to enum structure, you cannot change the names of these
 * 
 */
public enum ArenaStatus {
    /**
     * 
     * This constant sets an arena as waiting/not being used
     * 
     */
    WAITING(ChatColor.translateAlternateColorCodes('&', "&aWAITING")),
    /**
     * 
     * This constant sets an arena as running/in use
     * 
     */
    RUNNING(ChatColor.translateAlternateColorCodes('&', "&eRUNNING")),
    /**
     * 
     * This constant sets an arena as disabled/not usable
     * 
     */
    DISABLED(ChatColor.translateAlternateColorCodes('&', "&cDISABLED"));
    /**
     * 
     * This constructor initilizes needed constants
     * 
     * @param string Has no use but is needed for the constants to work right
     */
    private ArenaStatus(String string) {}
    /**
     * 
     * This method gets the given type from a string
     * 
     * @param string The string to read
     * @return The arenastatus found, if there is one.
     */
    public static ArenaStatus getFromString(String string) {
        //check for waiting
        if (string.equalsIgnoreCase("waiting")) {
            return WAITING;
        }
        //check for running
        else if (string.equalsIgnoreCase("running")) {
            return RUNNING;
        }
        //check for disabled
        else if (string.equalsIgnoreCase("disabled")) {
            return DISABLED;
        }
        return null;
    }
}
