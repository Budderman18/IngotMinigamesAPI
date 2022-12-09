package com.budderman18.IngotMinigamesAPI.Core.Handlers;

/**
 *
 * This enum handles the names of all valid death options
 * 
 */
public enum DeathMessageType {
    /**
     * 
     * This constant represents a sword kill
     * 
     */
    SWORD(),
    /**
     * 
     * This constant represents an axe kill
     * 
     */
    AXE(),
    /**
     * 
     * This constant represents an arrow kill
     * 
     */
    ARROW(),
    /**
     * 
     * This constant represents a firework kill
     * 
     */
    FIREWORK(),
    /**
     * 
     * This constant represents a trident kill
     * 
     */
    TRIDENT(),
    /**
     * 
     * This constant represents a potion kill
     * 
     */
    POTION(),
    /**
     * 
     * This constant represents a non-wepond melee kill
     * 
     */
    HAND(),
    /**
     * 
     * This constant represents a fall damage kill
     * 
     */
    FALL(),
    /**
     * 
     * This constant represents an explosion kill
     * 
     */
    EXPLOSION(),
    /**
     * 
     * This constant represents a falling block kill
     * 
     */
    CRUSHED(),
    /**
     * 
     * This constant represents a fire kill
     * 
     */
    FIRE(),
    /**
     * 
     * This constant represents a lava kill
     * 
     */
    LAVA(),
    /**
     * 
     * This constant represents a drown kill
     * 
     */
    DROWN(),
    /**
     * 
     * This constant represents a suffication kill
     * 
     */
    SUFFICATE(),
    /**
     * 
     * This constant represents a worldborder kill
     * 
     */
    BORDER(),
    /**
     * 
     * This constant represents a kill with an unknown cause
     * 
     */
    OTHER();
    /**
     * 
     * This constructor initilizes needed constants
     * 
     */
    private DeathMessageType() {}
    /**
     * 
     * This method gets the given type from a string
     * 
     * @param string The string to read
     * @return The death type found, returns other if not found
     */
    public static DeathMessageType getFromString(String string) {
        //check for sword
        if (string.equalsIgnoreCase("sword")) {
            return SWORD;
        }
        //check for axe
        if (string.equalsIgnoreCase("axe")) {
            return AXE;
        }
        //check for arrow
        if (string.equalsIgnoreCase("arrow")) {
            return ARROW;
        }
        //check for firework
        if (string.equalsIgnoreCase("firework")) {
            return FIREWORK;
        }
        //check for trident
        if (string.equalsIgnoreCase("trident")) {
            return TRIDENT;
        }
        //check for potion
        if (string.equalsIgnoreCase("potion")) {
            return POTION;
        }
        //check for hand
        if (string.equalsIgnoreCase("hand")) {
            return HAND;
        }
        //check for fall
        if (string.equalsIgnoreCase("fall")) {
            return FALL;
        }
        //check for explosion
        if (string.equalsIgnoreCase("explosion")) {
            return EXPLOSION;
        }
        //check for crushed
        if (string.equalsIgnoreCase("crushed")) {
            return CRUSHED;
        }
        //check for fire
        if (string.equalsIgnoreCase("fire")) {
            return FIRE;
        }
        //check for lava
        if (string.equalsIgnoreCase("lava")) {
            return LAVA;
        }
        //check for drown
        if (string.equalsIgnoreCase("drown")) {
            return DROWN;
        }
        //check for sufficate
        if (string.equalsIgnoreCase("sufficate")) {
            return SUFFICATE;
        }
        //check for border
        if (string.equalsIgnoreCase("border")) {
            return BORDER;
        }
        return OTHER;
    }
}
