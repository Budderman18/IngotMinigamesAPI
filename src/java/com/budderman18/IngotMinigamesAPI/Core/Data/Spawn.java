package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 *
 * This method handles spawns for players
 * You can create your own as well as teleport to them
 * Includes random teleportation
 * 
 */
public class Spawn {
    /**
     *
     * This constructor blocks new() usage
     * This forces usage of create() message
     * 
     */
    private Spawn() {}
    //spawn vars
    private String name = null;
    private double[] location = new double[5];
    private int index = 0;
    private boolean isOccupied = false;
    private Plugin plugin = null;
    //global vars
    private static List<Spawn> spawns = new ArrayList<>();
    private static int trueIndex = 0;
    private static Plugin staticPlugin = Main.getInstance();
    private static FileConfiguration config = FileManager.getCustomData(staticPlugin, "config", "");
    /**
     *
     * This method creates a new spawn.
     * Spawns support decimals. 
     *
     * @param namee The spawn's name
     * @param x The spawn's x pos
     * @param y The spawn's y pos
     * @param z The spawn's z pos
     * @param yaw The spawn's yaw rotation
     * @param pitch The spawn's pitch rotation
     * @param pluginn The plugin to attach to
     * @return the generated spawns
     */
    public static Spawn createSpawn(String namee, double x, double y, double z, double yaw, double pitch, Plugin pluginn) {
        //create spawn object
        Spawn newSpawn = new Spawn();
        //create locaiton array
        newSpawn.location[0] = x;
        newSpawn.location[1] = y;
        newSpawn.location[2] = z;
        newSpawn.location[3] = yaw;
        newSpawn.location[4] = pitch;
        //save variables
        newSpawn.name = namee;
        newSpawn.index = trueIndex;
        newSpawn.plugin = pluginn;
        //return spawn
        spawns.add(newSpawn);
        trueIndex++;
        return newSpawn;
    }
    /**
     *
     * This method deletes the selected spawn. 
     *
     */
    public void deleteSpawn() {
        //decrement all higher indexes to prevent bugs
        for (Spawn key : spawns) {
            if (key.index > this.index) {
                spawns.get(key.index).index--;
            }
        }
        //reset variables
        this.location[0] = 0;
        this.location[1] = 0;
        this.location[2] = 0;
        this.location[3] = 0;
        this.location[4] = 0;
        this.name = null;
        this.index = 0;
        this.isOccupied = false;
        this.plugin = null;
        //remove spawn from instance list
        trueIndex--;
        spawns.remove(trueIndex);
    }
    /**
     *
     * This method selects a given spawn if it exists
     *
     * @param namee the spawn to search for
     * @param pluginn the plugin to ensure its attached to
     * @return the selected spawn
     */
    public static Spawn selectSpawn(String namee, Plugin pluginn) {
        //cycle through all loaded instances
        for (Spawn key : spawns) {
            //check if spawn name is not null
            if (key.getName() != null) {
                //check if spawn name if equal to namee
                if (key.name.equals(namee) && key.plugin == pluginn) {
                    //return selection
                    return key;
                }
            }
        }
        //output error message
        if (config.getBoolean("enable-debug-mode") == true) {
            Logger.getLogger(Spawn.class.getName()).log(Level.SEVERE, null, "COULD NOT LOAD SPAWN " + namee + '!');
        }
        //return arena
        return null;
    }
    /**
     * 
     * This method gets all loaded instances
     * Instances with null names are ignored (they shouldn't exist)
     * 
     * @return The list of spawns
     */
    public static List<Spawn> getInstances() {
        //local vars
        List<Spawn> spawnss = new ArrayList<>();
        //cycle through all spwans
        for (Spawn key : spawns) {
            //check if spawn name isnt null
            if (key.name != null) {
                //add spawn
                spawnss.add(key);
            }
            else {
                //delete invalid spawn
                key.deleteSpawn();
            }
        }
        //return list
        return spawnss;
    }
    /**
     *
     * This method teleports the given entity to the selected spawn. 
     *
     * @param entity The entity to teleport
     */
    public void moveToSpawn(Entity entity) {
        //create location from spawn
        Location location = new Location(Bukkit.getWorld("world"), this.location[0], this.location[1], this.location[2], (float) this.location[3], (float) this.location[4]);
        //teleport
        entity.teleport(location);
    }
    /**
     *
     * This method teleports the given entity to a 
     * random spawn in the designated list. 
     *
     * @param spawns the spawns to randomize
     * @param entity The entity to teleport
     * @return 
     */
    public static Spawn moveToRandomSpawn(List<Spawn> spawns, Entity entity) {
        //call random class
        Random random = new Random(); 
        //local vars
        long start = 0;
        long end = 0;
        long temploc = 0;
        byte infcheck = 0;
        //temporary spawn
        Spawn spawn = null;
        //determine end number
        end = spawns.size();
        //randomize
        do {
            temploc = random.nextLong(start, end);
            //set spawn to new random spawn
            spawn = spawns.get((int) temploc);
            infcheck++;
        } while (spawn.getIsOccupied() == true || infcheck != 127);
        //teleport
        spawn.moveToSpawn(entity);
        spawn.setIsOccupied(true);
        return spawn;
    }
    /**
     *
     * This method sets the name of the selected spawn. 
     * setting to null is not allowed since it breaks selections
     *
     * @param namee The name to set
     */
    public void setName(String namee) {
        //check if name is null
        if (namee != null) {
            //set instance list
            if (spawns.contains(this)) {
                spawns.get(this.index).name = namee;
            }
            //set selection
            this.name = namee;
        }
        else {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SET THE NAME FOR ARENA " + this.name + '!');
            }
        }
    }
    /**
     *
     * This method obtains the name of the selected spawn. 
     *
     * @return the spawn name
     */
    public String getName() {
        //return instance list
        if (spawns.contains(this)) {
            return spawns.get(this.index).name;
        }
        //return selection as a fallback
        return this.name;
    }
    /**
     *
     * This method sets the location of the selected spawn. 
     *
     * @param locationn The location array to set
     */
    public void setLocation(double[] locationn) {
        //set instance list
        if (spawns.contains(this)) {
            spawns.get(this.index).location = locationn;
        }
        //set selection
        this.location = locationn;
    }
    /**
     *
     * This method obtains the location of the selected spawn
     *
     * @return the location array
     */
    public double[] getLocation() {
        //return instance list
        if (spawns.contains(this)) {
            return spawns.get(this.index).location;
        }
        //return selection as a fallback
        return this.location;
    }
    /**
     *
     * This method sets the isOccupied of the selected spawn. 
     *
     * @param isOccupiedd the isOccupied state
     */
    public void setIsOccupied(boolean isOccupiedd) {
        //set instance array
        if (spawns.contains(this)) {
            spawns.get(this.index).isOccupied = isOccupiedd;
        }
        //set selection
        this.isOccupied = isOccupiedd;
    }
    /**
     *
     * This method obtains the isOccupied of the selected spawn
     *
     * @return the isOccupied state
     */
    public boolean getIsOccupied() {
        //return instance list
        if (spawns.contains(this)) {
            return spawns.get(this.index).isOccupied;
        }
        //return selection as a fallback
        return this.isOccupied;
    }
    /**
     *
     * This method sets the isOccupied of the selected spawn. 
     *
     * @param pluginn the isOccupied state
     */
    public void setPlugin(Plugin pluginn) {
        //set instance array
        if (spawns.contains(this)) {
            spawns.get(this.index).plugin = pluginn;
        }
        //set selection
        this.plugin = pluginn;
    }
    /**
     *
     * This method obtains the isOccupied of the selected spawn
     *
     * @return the plugin state
     */
    public Plugin getPlugin() {
        //return instance list
        if (spawns.contains(this)) {
            return spawns.get(this.index).plugin;
        }
        //return selection as a fallback
        return this.plugin;
    }
}