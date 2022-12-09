package com.budderman18.IngotMinigamesAPI.Addons;

/**
 * 
 * This enum declares animations that can be used for supply drops.
 * 
 */
public enum ChestAnimation {
    /**
     * 
     * This constant dictates a skyfall animation
     * This consists of a fake player (a zombie for now since FakePlayer doesnt work yet)
     * flying overhead, holding the chest, and dropping it as a falling block.
     * When landing, the area explodes a bit
     * 
     */
    SKYFALL(),
    /**
     * 
     * This constant dictates a delivery animation
     * This constists of a player on a horse walking by, and
     * setting the chest on the ground.
     * The horse + player are invincable
     * 
     */
    DELIVERY(),
    /**
     * 
     * This constant dictates a beacon animation
     * This consists of a beacon summoning with a beam, and
     * slowly summons explosions as the chest glides down on
     * the beacon. When it lands, it does 1 last, larger explosion
     * 
     */
    BEACON(),
    /**
     * 
     * This constant dictates a drill animation
     * This consists of explosions rumbling the ground, and
     * after a while, the chest pops out of the ground and into
     * the air, then falls back down, causing one last explosion
     * 
     */
    DRILL();
    /**
     * 
     * This method gets the given type from a string
     * 
     * @param string The string to read
     * @return The animation found, if there is one.
     */
    public static ChestAnimation getFromString(String string) {
        //check for skyfall
        if (string.equalsIgnoreCase("skyfall")) {
            return SKYFALL;
        }
        //check for delivery
        else if (string.equalsIgnoreCase("delivery")) {
            return DELIVERY;
        }
        //check for beacon
        else if (string.equalsIgnoreCase("beacon")) {
            return BEACON;
        }
        //check for drill
        else if (string.equalsIgnoreCase("drill")) {
            return DRILL;
        }
        return null;
    }
}
