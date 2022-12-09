package com.budderman18.IngotMinigamesAPI.Addons;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TimerHandler;
import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 *
 * This class handles the worldborder for IMAPI
 * This can be either the custom-made border or the vanilla one
 * 
 */
public class GameBorder {
    //border vars
    private String name = "";
    private double radius = 0.0;
    private Location center = null;
    private EntityType borderEntity = EntityType.AREA_EFFECT_CLOUD;
    private double summonEntities = 0.0;
    private short summonTime = 0;
    private float entityDamage = (float) 0.0;
    private float entityKnockback = (float) 0.0;
    private Arena arena = null;
    private boolean useVanillaBorder = false;
    private int index = 0;
    private Plugin plugin = null;
    //global vars
    private static Plugin staticPlugin = Main.getInstance();
    private static FileConfiguration config = FileManager.getCustomData(staticPlugin, "config", "");
    private static List<GameBorder> borders = new ArrayList<>();
    private static int trueIndex = 0;
    private static String namee = "";
    /**
     * 
     * This constructor blocks new() usage
     * It will force people to use create() instead
     * 
     */
    private GameBorder() {}
    /**
     *
     * This method creates a new border.
     *
     * @param namee The spawn's name
     * @param radiuss The radius of the border
     * @param centerr the center location of the border
     * @param borderEntityy the entity summoned when approching the edge (custom only)
     * @param pluginn The plugin to attach to
     * @param summonEntitiess how close you need to be to summon entities (custom only)
     * @param summonTimee how much time in-between summons (in ticks) (custom only)
     * @param entityKnockbackk how much force should be applied when the entity hits the player (custom only)
     * @param arenaa the arena to attach to
     * @param useVanilla true to use vanilla worldborder (more limited)
     * @param entityDamagee how much damage to inflict when the entity hits the player (custom only)
     * @return the generated border
     */
    public static GameBorder createBorder(String namee, double radiuss, Location centerr, EntityType borderEntityy, double summonEntitiess, float entityDamagee, short summonTimee, float entityKnockbackk, Arena arenaa, boolean useVanilla, Plugin pluginn) {
        //create spawn object
        GameBorder newBorder = new GameBorder();
        //save variables
        newBorder.name = namee;
        newBorder.radius = radiuss;
        newBorder.center = centerr;
        newBorder.borderEntity = borderEntityy;
        newBorder.summonEntities = summonEntitiess;
        newBorder.entityDamage = entityDamagee;
        newBorder.summonTime = summonTimee;
        newBorder.entityKnockback = entityKnockbackk;
        newBorder.arena = arenaa;
        newBorder.useVanillaBorder = useVanilla;
        newBorder.index = trueIndex;
        newBorder.plugin = pluginn;
        //return border
        borders.add(newBorder);
        trueIndex++;
        return newBorder;
    }
    /**
     *
     * This method deletes the selected border. 
     *
     */
    public void deleteBorder() {
        //decrement all higher indexes to prevent bugs
        for (GameBorder key : borders) {
            if (key.index > this.index) {
                borders.get(key.index).index--;
            }
        }
        //reset variables
        this.name = null;
        this.center = null;
        this.radius = 0;
        this.borderEntity = null;
        this.summonEntities = 0;
        this.entityDamage = 0;
        this.summonTime = 0;
        this.entityKnockback = 0;
        this.arena = null;
        this.useVanillaBorder = false;
        this.index = 0;
        this.plugin = null;
        //remove spawn from instance list
        trueIndex--;
        borders.remove(trueIndex);
    }
    /**
     *
     * This method selects a given spawn if it exists
     *
     * @param namee the spawn to search for
     * @param pluginn the plugin to ensure its attached to
     * @return the selected spawn
     */
    public static GameBorder selectBorder(String namee, Plugin pluginn) {
        //cycle through all loaded instances
        for (GameBorder key : borders) {
            //check if spawn name is not null
            if (key.name != null) {
                //check if spawn name if equal to namee
                if (key.name.equals(namee) && key.plugin == pluginn) {
                    //return selection
                    return key;
                }
            }
        }
        //output error message
        if (config.getBoolean("enable-debug-mode") == true) {
            Logger.getLogger(GameBorder.class.getName()).log(Level.SEVERE, null, "COULD NOT LOAD SPAWN " + namee + '!');
        }
        //return arena
        return null;
    }
    /**
     * 
     * This method gets all loaded instances
     * Instances with null names are ignored (they shouldn't exist)
     * 
     * @return The list of borders
     */
    public static List<GameBorder> getInstances() {
        //local vars
        List<GameBorder> borderss = new ArrayList<>();
        //cycle through all spwans
        for (GameBorder key : borders) {
            //check if spawn name isnt null
            if (key.name != null) {
                //add spawn
                borderss.add(key);
            }
            else {
                //delete invalid border
                key.deleteBorder();
            }
        }
        //return list
        return borderss;
    }
    /**
     * 
     * This method modifies the border size over a given time
     * Perfect for smooth transitions between sizes
     * 
     * @param newRadius the new radius to set
     * @param time the time it'll take to set in ticks
     */
    public void modifyBorderSize(double newRadius, short time) {
        //local vars
        Runnable action = () -> {
            //runnable vars
            double radiuss = this.getRadius();
            double change = this.getRadius() - newRadius;
            //check if decreasing
            if (newRadius < this.getRadius()) {
                radiuss -= change/time;
            }
            //check if increasing
            if (newRadius > this.getRadius()) {
                radiuss += change/time;
            }
            //set updated radius
            this.setRadius(radiuss);
            //check if done changing
            if (radiuss != newRadius) {
                //run again
                this.modifyBorderSize(newRadius, (short) (time-1));
            }
        };
        //check if using vanilla worldborder
        if (this.useVanillaBorder == true) {
            //set border size and stop method
            this.center.getWorld().getWorldBorder().setSize(newRadius, time);
            return;
        }
        //run again
        TimerHandler.runTimer(this.plugin, 0, 1, action, true, true);
    }
    /**
     * 
     * This method will summon border entities at a given player (if they're in range)
     * 
     * @param player the player to check
     * @param checkOnce false to have it run itself until the player is gone
     * @return the entity's custom name (which is just the border name), "" if none was summoned. This should be used in events for damage and knockback modification
     */
    public String attackPlayer(Player player, boolean checkOnce) {
        //local vars
        Runnable action = () -> {
            //runnable vars
            Location locToCheck = player.getLocation();
            Entity entity = null;
            double xloc = 0;
            double zloc = 0;
            double diff = 0;
            //set y pos
            locToCheck.setY(player.getLocation().getY() + 10);
            //set offsets
            xloc = locToCheck.getX() - this.getCenter().getX();
            zloc = locToCheck.getZ() - this.getCenter().getZ();
            //check if z is negative
            if (zloc < 0) {
                //absolute value
                zloc = zloc * -1;
            }
            //check if x is negative
            if (xloc < 0) {
                //absolute value
                xloc = xloc * -1;
            }
            //obtain difference
            diff = zloc - xloc;
            //revert changes
            xloc = locToCheck.getX() + this.getCenter().getX();
            zloc = locToCheck.getZ() + this.getCenter().getZ();
            //check if along western edge
            if ((xloc >= zloc && xloc > this.getCenter().getX()) && diff <= 0) {
                //set spawn loc
                locToCheck.setX(locToCheck.getX() + this.summonEntities);
                //spawn and launch entity
                entity = this.center.getWorld().spawnEntity(locToCheck, this.borderEntity);
                entity.setVelocity(new Vector(-4,-((this.summonEntities*0.4)*0.875), 0));
            }
            //check if along northern edge
            else if ((zloc >= xloc && zloc > this.getCenter().getZ()) && diff > 0) {
                //set spawn loc
                locToCheck.setZ(locToCheck.getZ() + this.summonEntities);
                //spawn and launch entity
                entity = this.center.getWorld().spawnEntity(locToCheck, this.borderEntity);
                entity.setVelocity(new Vector(0,-((this.summonEntities*0.4)*0.875), -4));
            }
            //check if along eastern edge
            else if ((xloc < zloc && xloc < this.getCenter().getX() || (xloc > zloc && xloc < this.getCenter().getX())) && diff <= 0) {
                //set spawn loc
                locToCheck.setX(locToCheck.getX() - this.summonEntities);
                //spawn and launch entity
                entity = this.center.getWorld().spawnEntity(locToCheck, this.borderEntity);
                entity.setVelocity(new Vector(4,-((this.summonEntities*0.4)*0.875), 0));
            }
            //check if along southern edge
            else if ((zloc < xloc && zloc < this.getCenter().getZ() || (zloc > xloc && zloc < this.getCenter().getZ())) && diff > 0) {
                //set spawn loc
                locToCheck.setZ(locToCheck.getZ() - this.summonEntities);
                //spawn and launch entity
                entity = this.center.getWorld().spawnEntity(locToCheck, this.borderEntity);
                entity.setVelocity(new Vector(0,-((this.summonEntities*0.4)*0.875), 4));
            }
            //set entity data
            entity.setCustomNameVisible(false);
            entity.setCustomName(this.name);
            namee = entity.getCustomName();
            //check if running again
            if (checkOnce == false) {
                this.attackPlayer(player, false);
            }
        };
        //check for in border
        if (this.isInBorder(player) == false) {
            //summon mob
            TimerHandler.runTimer(this.plugin, 0, this.summonTime, action, true, false);
            return namee;
        }
        //return blank string to prevent problems with null ones
        return "";
    }
    /**
     * 
     * This method checks if a player is inside the border
     * 
     * @param player the player to check
     * @return true if in border
     */
    public boolean isInBorder(Player player) {
        return !((player.getLocation().getX() < (this.getCenter().getX() - this.getRadius()) || player.getLocation().getZ() < (this.getCenter().getZ() - this.getRadius())) || (player.getLocation().getX() > (this.getCenter().getX() + this.getRadius()) || player.getLocation().getZ() > (this.getCenter().getZ() + this.getRadius())));
    }
    /**
     *
     * This method changes the name of the current border.
     * It will prevent setting to null, which breaks selections
     *
     * @param namee the name to set
     */
    public void setName(String namee) {
        //check if name is null
        if (namee != null) {
            //set instance list
            if (borders.contains(this)) {
                borders.get(this.index).name = namee;
            }
            //set selection
            this.name = namee;
        }
        else {
            //check if running debug mode
            if (config.getBoolean("enable-debug-mode") == true) {
                //log error
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SET THE NAME FOR ARENA " + this.name + '!');
            }
        }
    }
    /**
     *
     * This method gets the current arena's name. 
     *
     * @return the arena name
     */
    public String getName() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).name;
        }
        //return selection as a fallback
        return this.name;
    }
    /**
     *
     * This method sets the center for the selected arena. 
     *
     * @param centerr the center to set
     */
    public void setCenter(Location centerr) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).center = centerr;
        }
        //set selection
        this.center = centerr;
    }
    /**
     *
     * This method gets the plugin for the selected arena. 
     *
     * @return the center object
     */
    public Location getCenter() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).center;
        }
        //return selection as a fallback
        return this.center;
    }
    /**
     *
     * This method sets the radius for the selected border. 
     *
     * @param radiuss the amount to set
     */
    public void setRadius(double radiuss) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).radius = radiuss;
        }
        //set selection
        this.radius = radiuss;
    }
    /**
     *
     * This method gets the radius for the selected border. 
     *
     * @return the radius
     */
    public double getRadius() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).radius;
        }
        //return selection as a fallback
        return this.radius;
    }
    /**
     *
     * This method sets the radius for the selected border. 
     *
     * @param entityy the entitytype to set
     */
    public void setBorderEntity(EntityType entityy) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).borderEntity = entityy;
        }
        //set selection
        this.borderEntity = entityy;
    }
    /**
     *
     * This method gets the radius for the selected border. 
     *
     * @return the entitytype
     */
    public EntityType getBorderEntity() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).borderEntity;
        }
        //return selection as a fallback
        return this.borderEntity;
    }
    /**
     *
     * This method sets the radius for the selected border. 
     *
     * @param summonn the amount to set
     */
    public void setSummonEntities(double summonn) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).summonEntities = summonn;
        }
        //set selection
        this.summonEntities = summonn;
    }
    /**
     *
     * This method gets the radius for the selected border. 
     *
     * @return the summonEntites
     */
    public double getSummonEntities() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).summonEntities;
        }
        //return selection as a fallback
        return this.summonEntities;
    }
    /**
     *
     * This method sets the radius for the selected border. 
     *
     * @param timee the amount to set
     */
    public void setSummonTime(short timee) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).summonTime = timee;
        }
        //set selection
        this.summonTime = timee;
    }
    /**
     *
     * This method gets the radius for the selected border. 
     *
     * @return the summontime
     */
    public short getSummonTime() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).summonTime;
        }
        //return selection as a fallback
        return this.summonTime;
    }
    /**
     *
     * This method sets the radius for the selected border. 
     *
     * @param damagee the amount to set
     */
    public void setEntityDamage(float damagee) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).entityDamage = damagee;
        }
        //set selection
        this.entityDamage = damagee;
    }
    /**
     *
     * This method gets the radius for the selected border. 
     *
     * @return the damage
     */
    public float getEntityDamage() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).entityDamage;
        }
        //return selection as a fallback
        return this.entityDamage;
    }
    /**
     *
     * This method sets the entityKncokback for the selected border. 
     *
     * @param knockk the amount to set
     */
    public void setEntityKnockback(float knockk) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).entityKnockback = knockk;
        }
        //set selection
        this.entityKnockback = knockk;
    }
    /**
     *
     * This method gets the entityknockback for the selected border. 
     *
     * @return the knockback
     */
    public float getEntityKnockback() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).entityKnockback;
        }
        //return selection as a fallback
        return this.entityKnockback;
    }
    /**
     *
     * This method sets the arena for the selected border. 
     *
     * @param arenaa the arena to set
     */
    public void setArena(Arena arenaa) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).arena = arenaa;
        }
        //set selection
        this.arena = arenaa;
    }
    /**
     *
     * This method gets the arena for the selected border. 
     *
     * @return the arena
     */
    public Arena getArena() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).arena;
        }
        //return selection as a fallback
        return this.arena;
    }
    /**
     *
     * This method sets the radius for the selected border. 
     *
     * @param usee the amount to set
     */
    public void setUseVanillaBorder(boolean usee) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).useVanillaBorder = usee;
        }
        //set selection
        this.useVanillaBorder = usee;
    }
    /**
     *
     * This method gets the useVanilla for the selected border. 
     *
     * @return the damage
     */
    public boolean getUseVanillaBorder() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).useVanillaBorder;
        }
        //return selection as a fallback
        return this.useVanillaBorder;
    }
    /**
     *
     * This method sets the plugin for the selected arena. 
     *
     * @param pluginn the plugin to set
     */
    public void setPlugin(Plugin pluginn) {
        //set instance list
        if (borders.contains(this)) {
            borders.get(this.index).plugin = pluginn;
        }
        //set selection
        this.plugin = pluginn;
    }
    /**
     *
     * This method gets the plugin for the selected arena. 
     *
     * @return the plugin object
     */
    public Plugin getPlugin() {
        //return instance list
        if (borders.contains(this)) {
            return borders.get(this.index).plugin;
        }
        //return selection as a fallback
        return this.plugin;
    }
}