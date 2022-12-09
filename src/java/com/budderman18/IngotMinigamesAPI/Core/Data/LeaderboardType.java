package com.budderman18.IngotMinigamesAPI.Core.Data;

/**
 *
 * This enum handles different types of leaderboards
 * 
 */
public enum LeaderboardType {
    /**
     * 
     * This constant represents kills
     * 
     */
    KILLS(),
    /**
     * 
     * This constant represents deaths
     * 
     */
    DEATHS(),
    /**
     * 
     * This constant represents wins
     * 
     */
    WINS(),
    /**
     * 
     * This constant represents losses
     * 
     */
    LOSSES(),
    /**
     * 
     * This constant represents kill/death
     * 
     */
    KDRATIO(),
    /**
     * 
     * This constant represents win/loss
     * 
     */
    WLRATIO(),
    /**
     * 
     * This constant represents score
     * 
     */
    SCORE();
    /**
     * 
     * This constructor initilizes needed constants
     * 
     */
    private LeaderboardType() {}
    /**
     * 
     * This method gets the given type from a string
     * 
     * @param string The string to read
     * @return The board type found, returns score if not found
     */
    public static LeaderboardType getFromString(String string) {
        //check if string is null
        if (string == null) {
            string = " ";
        }
        //check for kills
        if (string.equalsIgnoreCase("kills")) {
            return KILLS;
        }
        //check for deaths
        if (string.equalsIgnoreCase("deaths")) {
            return DEATHS;
        }
        //check for wins
        if (string.equalsIgnoreCase("wins")) {
            return WINS;
        }
        //check for losses
        if (string.equalsIgnoreCase("losses")) {
            return LOSSES;
        }
        //check for kdratio
        if (string.equalsIgnoreCase("kdratio")) {
            return KDRATIO;
        }
        //check for wlratio
        if (string.equalsIgnoreCase("wlratio")) {
            return WLRATIO;
        }
        return SCORE;
    }
}