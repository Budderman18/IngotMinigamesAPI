package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Core.MissingBukkitMethods;
import com.budderman18.IngotMinigamesAPI.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles everything involving arenas. 
 * This includes physical instances, schematics and settings. 
 * 
 */
public class Arena {
    /**
     *
     * This constructor does absolutely nothing.
     * If you want to create new arenas, use create() method!
     * This is only public so the class can be extended.
     * 
     */
    @Deprecated
    public Arena() {}
    //arena vars
    private int[] pos1 = new int[3];
    private int[] pos2 = new int[3];
    private String world = null;
    private String name = "ERROR";
    private byte minPlayers = 0;
    private byte skipPlayers = 0;
    private byte maxPlayers = 0;
    private List<Spawn> spawns = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private byte teamSize = 0;
    private double[] centerPos = new double[5];
    private double[] lobbyPos = new double[5];
    private String lobbyWorld = "";
    private double[] exitPos = new double[5];
    private String exitWorld = "";
    private double[] specPos = new double[5];
    private int lobbyWaitTime = 0;
    private int gameLengthTime = 0;
    private int gameWaitTime = 0;
    private int lobbySkipTime = 0;
    private int index = 0;
    private ArenaStatus status = ArenaStatus.WAITING;
    private Permission arenaPerm = null;
    private byte currentPlayers = 0;
    private int currentSpawn = 1;
    private String filePath = null;
    private Plugin plugin = null;
    //global vars
    private static Plugin staticPlugin = Main.getInstance();
    private static FileConfiguration config = FileManager.getCustomData(staticPlugin, "config", "");
    private static List<Arena> arenas = new ArrayList<>();
    private static int trueIndex = 0;
    /**
     *
     * This method creates a new arena.It will also save the settings file.Use createArenaSchematic() for the region file
     *
     * @param pos1 the negative-most corner of the arena
     * @param pos2 the positive-most corner of the arena
     * @param worldd the world name the arena is in
     * @param arenaName the arena's name
     * @param minPlayerss the minimum amount of players that can play
     * @param skipPlayerss the amount of players needed to shorten the wait timer
     * @param maxPlayerss the max amount of players that can play
     * @param teamSizee the max team size
     * @param lobbyWaitTimee the amount of time the lobby lasts
     * @param gameWaitTimee the amount of time needed for the game to start once entered
     * @param gameLengthh the length of the game
     * @param lobbySkipTimee the amount of time set to when skipPLayers is reached
     * @param lobbyWorldd the world name for the lobby
     * @param filePathh the filePath for the arena's files
     * @param exitWorldd the world name players get moved to when leaving
     * @param exitPoss the position players get moved to when leaving
     * @param centerPoss the center of the arena
     * @param lobbyPoss the position players get teleported to when joining
     * @param specPoss the position players are teleported to when spectating
     * @param saveFilee weather or not to save a settings file
     * @param pluginn the plugin to attach this arena to
     * @return The arena object that was generated
     */
    public static Arena createArena(int[] pos1, int[] pos2, String worldd, String arenaName, byte minPlayerss, byte skipPlayerss, byte maxPlayerss, byte teamSizee, int lobbyWaitTimee, int lobbySkipTimee, int gameWaitTimee, int gameLengthh, double[] lobbyPoss, String lobbyWorldd, double[] exitPoss, String exitWorldd, double[] specPoss, double[] centerPoss, String filePathh, boolean saveFilee, Plugin pluginn) {
        //create arena object
        Arena newArena = new Arena();
        //files
        File arenaDataf = new File(pluginn.getDataFolder() + filePathh, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(pluginn, "settings", filePathh);
        //set arrays to prevent errors
        if (lobbyPoss == null) {
            lobbyPoss = new double[5];
        }
        if (exitPoss == null) {
            exitPoss = new double[5];
        }
        if (specPoss == null) {
            specPoss = new double[5];
        }
        if (centerPoss == null) {
            centerPoss = new double[5];
        }
        //check if saveFile is true
        if (saveFilee == true) {
            //set file
            arenaData.set("pos1.x", pos1[0]);
            arenaData.set("pos1.y", pos1[1]);
            arenaData.set("pos1.z", pos1[2]);
            arenaData.set("pos2.x", pos2[0]);
            arenaData.set("pos2.y", pos2[1]);
            arenaData.set("pos2.z", pos2[2]);
            arenaData.set("world", worldd);
            arenaData.set("name", arenaName);
            arenaData.set("minPlayers", minPlayerss);
            arenaData.set("skipPlayers", skipPlayerss);
            arenaData.set("maxPlayers", maxPlayerss);
            arenaData.set("teamSize", teamSizee);
            arenaData.set("lobby-wait-time", lobbyWaitTimee);
            arenaData.set("lobby-skip-time", lobbySkipTimee);
            arenaData.set("game-wait-time", gameWaitTimee);
            arenaData.set("game-length-time", gameLengthh);
            arenaData.set("Lobby.world", lobbyWorldd);
            arenaData.set("Lobby.x", lobbyPoss[0]);
            arenaData.set("Lobby.y", lobbyPoss[1]);
            arenaData.set("Lobby.z", lobbyPoss[2]);
            arenaData.set("Lobby.yaw", lobbyPoss[3]);
            arenaData.set("Lobby.pitch", lobbyPoss[4]);
            arenaData.set("Exit.world", exitWorldd);
            arenaData.set("Exit.x", exitPoss[0]);
            arenaData.set("Exit.y", exitPoss[1]);
            arenaData.set("Exit.z", exitPoss[2]);
            arenaData.set("Exit.yaw", exitPoss[3]);
            arenaData.set("Exit.pitch", exitPoss[4]);
            arenaData.set("Spec.x", specPoss[0]);
            arenaData.set("Spec.y", specPoss[1]);
            arenaData.set("Spec.z", specPoss[2]);
            arenaData.set("Spec.yaw", specPoss[3]);
            arenaData.set("Spec.pitch", specPoss[4]);
            arenaData.set("Center.x", centerPoss[0]);
            arenaData.set("Center.y", centerPoss[1]);
            arenaData.set("Center.z", centerPoss[2]);
            arenaData.set("Center.yaw", centerPoss[3]);
            arenaData.set("Center.pitch", centerPoss[4]);
            //save file
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                if (config.getBoolean("enable-debug-mode") == true) {
                    Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SAVE ARENA " + arenaName + '!');
                }
            }
        }
        //set fields
        newArena.pos1 = pos1;
        newArena.pos2 = pos2;
        newArena.world = worldd;
        newArena.name = arenaName;
        newArena.minPlayers = minPlayerss;
        newArena.skipPlayers = skipPlayerss;
        newArena.maxPlayers = maxPlayerss;
        newArena.teamSize = teamSizee;
        newArena.filePath = filePathh;
        newArena.index = trueIndex;
        newArena.lobbyWaitTime = lobbyWaitTimee;
        newArena.lobbySkipTime = lobbySkipTimee;
        newArena.gameWaitTime = gameWaitTimee;
        newArena.gameLengthTime = gameLengthh;
        newArena.lobbyPos = lobbyPoss;
        newArena.lobbyWorld = lobbyWorldd;
        newArena.exitPos = exitPoss;
        newArena.exitWorld = exitWorldd;
        newArena.specPos = specPoss;
        newArena.centerPos = centerPoss;
        newArena.plugin = pluginn;
        //add permission
        newArena.arenaPerm = new Permission("ingotsg.arenas." + newArena.name);
        Bukkit.getPluginManager().addPermission(newArena.arenaPerm);
        //add and return arena
        arenas.add(newArena);
        trueIndex++;
        return newArena;
    }
    /**
     *
     * This method deletes the selected arena. 
     *
     * @param deleteFiles true will delete the files in the arena
     */
    public void deleteArena(boolean deleteFiles) {
        //check for deleteFiles
        if (deleteFiles == true) {
            //delete files
            new File(plugin.getDataFolder() + this.filePath + "/settings.yml").delete();
            new File(plugin.getDataFolder() + this.filePath + "/region.arena").delete();
            new File(plugin.getDataFolder() + this.filePath).delete();
        }
        //decrement all higher indexes to prevent bugs
        for (Arena key : arenas) {
            if (key.index > this.index) {
                arenas.get(key.index).index--;
            }
        }
        //reset data
        arenas.remove(this.index);
        this.pos1[0] = 0;
        this.pos1[1] = 0;
        this.pos1[2] = 0;
        this.pos2[0] = 0;
        this.pos2[1] = 0;
        this.pos2[2] = 0;
        this.world = "";
        this.name = "";
        this.filePath = "";
        this.index = 0;
        Bukkit.getPluginManager().removePermission(this.arenaPerm);
        this.arenaPerm = null;
        this.maxPlayers = 0;
        this.skipPlayers = 0;
        this.minPlayers = 0;
        this.teamSize = 0;
        this.lobbyPos = null;
        this.lobbyWorld = null;
        this.exitPos = null;
        this.exitWorld = null;
        this.specPos = null;
        this.centerPos = null;
        this.lobbyWaitTime = 0;
        this.lobbySkipTime = 0;
        this.gameWaitTime = 0;
        this.gameLengthTime = 0;
        this.spawns = null;
        this.teams = null;
        trueIndex--;
    }
    /**
     *
     * This method selects and returns a given arena.Useful for swapping between loaded arenas. 
     *
     * @param namee the name to use when searching
     * @param pluginn the plugin to use when searching
     * @return the arena that was located
     */
    public static Arena selectArena(String namee, Plugin pluginn) {
        //cycle between all instances of arena
        for (Arena key : arenas) {
            //check if arena incstanc e name isn't null
            if (key.getName() != null) {
                //check if arena is what is requested
                if (key.getName().equals(namee) && key.getPlugin() == pluginn) {
                    //set selection data
                    return key;
                }
            }
        }
        //send error and return null
        if (config.getBoolean("enable-debug-mode") == true && (namee != null || !"".equals(namee))) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT LOAD ARENA " + namee + '!');
        }
        return null;
    }
    /**
     * 
     * This method gets all loaded instances
     * Instances with null names are ignored (they shouldn't exist)
     * 
     * @return The list of arenas
     */
    public static List<Arena> getInstances() {
        //local vars
        List<Arena> arenass = new ArrayList<>();
        //cycle through instances
        for (Arena key : arenas) {
            //check if name isnt null
            if (key.name != null) {
                //add arena
                arenass.add(key);
            }
            else {
                //delete arena if null name
                key.deleteArena(false);
            }
        }
        //return list
        return arenass;
    }
    /**
     *
     * This method creates the .arena file. 
     * It is recommended to use this on every new arena, 
     * since a schematic is NOT created in createArena method. 
     * You do not need one for every arena if you don't need any data from it. 
     * .arena files take out a good amount of disk space.
     *
     */
    public void createArenaSchematic() {
        //files
        File schem = new File(this.plugin.getDataFolder() + this.filePath, "region.arena");
        FileConfiguration schematic = FileManager.getCustomData(this.plugin, "region.arena", this.filePath);
        //local vars
        World worldd = Bukkit.getWorld(this.world);
        List<String> saveblocks = new ArrayList<>();
        List<String> saveentities = new ArrayList<>();
        int[] center = new int[3];
        String tempValue = " ";
        String lastValue = " ";
        Sign sign = null;
        String[] text = new String[4];
        String[] lastText = new String[4];
        Chest chest = null;
        Furnace furnace = null;
        Dropper dropper = null;
        Hopper hopper = null;
        BrewingStand stand = null;
        List<ItemStack> contents = new ArrayList<>();
        String contentString = "";
        String lastContentString = " ";
        String entityData = "";
        ArmorStand astand = null;
        ItemFrame frame = null;
        StorageMinecart cart = null;
        Painting paint = null;
        Piglin piglin = null;
        PiglinBrute brute = null;
        Skeleton skeleton = null;
        Stray stray = null;
        Villager villager = null;
        WanderingTrader trader = null;
        Zombie zombie = null;
        ZombieVillager zvillager = null;
        List<MerchantRecipe> trades = null;
        List<ItemStack> trade = null;
        int currentIndex = 0;
        //add empty string and start countings
        saveblocks.add("");
        currentIndex++;
        //cycle x coords
        for (int x = this.pos1[0] ; x < this.pos2[0] ; x++) {
            //cycle y coords
            for (int y = this.pos1[1] ; y < this.pos2[1] ; y++) {
                //cycle z corrds
                for (int z = this.pos1[2] ; z < this.pos2[2] ; z++) {
                    //temporary value
                    tempValue = worldd.getBlockAt(x, y, z).getBlockData().getAsString().replace("minecraft:", "");
                    //check if current index is equal to tempvalue
                    if (!lastValue.equalsIgnoreCase(tempValue) && !(tempValue.contains("sign") || tempValue.contains("barrel") || tempValue.contains("blast_furnace") || tempValue.contains("brewing_stand") || tempValue.contains("chest") || tempValue.contains("dispenser") || tempValue.contains("dropper") || tempValue.contains("furnace") || tempValue.contains("hopper") || tempValue.contains("shulker") || tempValue.contains("smoker") || tempValue.contains("trapped_chest"))) {
                        //set block
                        saveblocks.add(worldd.getBlockAt(x, y, z).getBlockData().getAsString().replace("minecraft:", "") + ";;" + x + ';' + y + ';' + z);
                        //save value for next reference
                        lastValue = tempValue;
                        currentIndex++;
                    }
                    //check if index is a sign
                    else if (!(lastValue.equalsIgnoreCase(tempValue)) && tempValue.contains("sign")) {
                        try {
                            //get sign text
                            sign = (Sign) worldd.getBlockAt(x, y, z).getState();
                            text = sign.getLines();
                            //check if text has changed
                            if (lastText != text) { 
                                //add text
                                saveblocks.add(worldd.getBlockAt(x, y, z).getBlockData().getAsString().replace("minecraft:", "") + ";{" + text[0] + '§' +  text[1] + '§' + text[2] + '§' + text[3] + "};" + x  + ';' + y + ';' + z);
                                //store and reset values
                                lastValue = tempValue;
                                lastText = text;
                                currentIndex++;
                            }
                        }
                        catch (ClassCastException ex) {}
                    }
                    //check if index is a container
                    else if (!(lastValue.equalsIgnoreCase(tempValue)) && (tempValue.contains("barrel") || tempValue.contains("blast_furnace") || tempValue.contains("brewing_stand") || tempValue.contains("chest") || tempValue.contains("dispenser") || tempValue.contains("dropper") || tempValue.contains("furnace") || tempValue.contains("hopper") || tempValue.contains("shulker") || tempValue.contains("smoker") || tempValue.contains("trapped_chest"))) {
                        //obtain if chest
                        try {
                            chest = (Chest) worldd.getBlockAt(x, y, z).getState();
                            contents.addAll(Arrays.asList(chest.getBlockInventory().getContents()));
                        } 
                        catch (ClassCastException ex) {}
                        //obtain if furnace
                        try {
                            furnace = (Furnace) worldd.getBlockAt(x, y, z).getState();
                            contents.addAll(Arrays.asList(furnace.getInventory().getContents()));
                        } 
                        catch (ClassCastException ex) {}
                        //obtain if dropper
                        try {
                            dropper = (Dropper) worldd.getBlockAt(x, y, z).getState();
                            contents.addAll(Arrays.asList(dropper.getInventory().getContents()));
                        } 
                        catch (ClassCastException ex) {}
                        //obtain if hopper
                        try {
                            hopper = (Hopper) worldd.getBlockAt(x, y, z).getState();
                            contents.addAll(Arrays.asList(hopper.getInventory().getContents()));
                        } 
                        catch (ClassCastException ex) {}
                        //obtain if stand
                        try {
                            stand = (BrewingStand) worldd.getBlockAt(x, y, z).getState();
                            contents.addAll(Arrays.asList(stand.getInventory().getContents()));;
                        } 
                        catch (ClassCastException ex) {}
                        //put into string
                        contentString = contents.toString();
                        //check if contatiner contents changed
                        if (!lastContentString.equals(contentString)) {
                            //store and reset values
                            lastContentString = contentString;
                            contentString = "";
                            //cycle through contents
                            contentString = MissingBukkitMethods.convertFromInventory(contents);
                            //add block
                            saveblocks.add(worldd.getBlockAt(x, y, z).getBlockData().getAsString().replace("minecraft:", "") + ";{" + contentString + "};" + x + ';' + y + ';' + z);
                            lastValue = tempValue;
                            currentIndex++;
                        }   
                    }
                }
            }
        }
        //obtain center location
        center[0] = (this.pos1[0] + this.pos2[0])/2;
        center[1] = (this.pos1[1] + this.pos2[1])/2;
        center[2] = (this.pos1[2] + this.pos2[2])/2;
        //cycle through entities
        for (Entity key : worldd.getNearbyEntities(new Location(worldd, center[0], center[1], center[2]), this.pos2[0]-center[0], this.pos2[1]-center[1], this.pos2[2]-center[2])) {
            //check if entity isnt a player
            if (key.getType() != EntityType.PLAYER) {
                //reset vars
                contents.clear();
                trades = null;
                contentString = "";
                //check for name
                if (key.getCustomName() != null) {
                    //obtain name
                    entityData = entityData.concat(key.getCustomName() + "¥");
                }
                if (key.getType() == EntityType.ARMOR_STAND) {
                    //obtain stand
                    astand = (ArmorStand) key;
                    //get inventory
                    contents.addAll(Arrays.asList(astand.getEquipment().getArmorContents()));
                    contents.add(astand.getEquipment().getItemInMainHand());
                    contents.add(astand.getEquipment().getItemInOffHand());
                }
                //check for item frame
                else if (key.getType() == EntityType.ITEM_FRAME || key.getType() == EntityType.GLOW_ITEM_FRAME) {
                    //obtain frame
                    frame = (ItemFrame) key;
                    //get held item
                    contents.add(frame.getItem());
                }
                //check for chest cart
                else if (key.getType() == EntityType.MINECART_CHEST) {
                    //obtain cart
                    cart = (StorageMinecart) key;
                    //get inventory
                    contents.addAll(Arrays.asList(cart.getInventory().getContents()));
                }
                //check for painting
                else if (key.getType() == EntityType.PAINTING) {
                    //obtain painting
                    paint = (Painting) key;
                    //get art type
                    entityData = entityData.concat(paint.getArt().toString() + "¥");
                }
                //check for piglin
                else if (key.getType() == EntityType.PIGLIN) {
                    //get piglin
                    piglin = (Piglin) key;
                    //obtain inventory
                    contents.addAll(Arrays.asList(piglin.getInventory().getContents()));
                }
                //check for brute
                else if (key.getType() == EntityType.PIGLIN_BRUTE) {
                    //obtain brute
                    brute = (PiglinBrute) key;
                    //get inventory
                    contents.addAll(Arrays.asList(brute.getEquipment().getArmorContents()));
                    contents.add(brute.getEquipment().getItemInMainHand());
                    contents.add(brute.getEquipment().getItemInOffHand());
                }
                //check for skeleton
                else if (key.getType() == EntityType.SKELETON) {
                    //obtain skeleton
                    skeleton = (Skeleton) key;
                    //obtain inventory
                    contents.addAll(Arrays.asList(skeleton.getEquipment().getArmorContents()));
                    contents.add(skeleton.getEquipment().getItemInMainHand());
                    contents.add(skeleton.getEquipment().getItemInOffHand());
                }
                //check for stray
                else if (key.getType() == EntityType.STRAY) {
                    //obtain stray
                    stray = (Stray) key;
                    //get inventory
                    contents.addAll(Arrays.asList(stray.getEquipment().getArmorContents()));
                    contents.add(stray.getEquipment().getItemInMainHand());
                    contents.add(stray.getEquipment().getItemInOffHand());
                }
                //check for villager
                else if (key.getType() == EntityType.VILLAGER) {
                    //obtain villager
                    villager = (Villager) key;
                    //get villager data
                    entityData = entityData.concat(villager.getProfession().toString() + "¥");
                    entityData = entityData.concat(villager.getVillagerType().toString() + "¥");
                    entityData = entityData.concat(villager.getVillagerLevel() + "¥");
                    entityData = entityData.concat(villager.getVillagerExperience() + "¥");
                    //get trades
                    trades = villager.getRecipes();
                }
                //check for wandering trader
                else if (key.getType() == EntityType.WANDERING_TRADER) {
                    //obtain trader
                    trader = (WanderingTrader) key;
                    //get trades
                    trades = trader.getRecipes();
                }
                //check for zombie
                else if (key.getType() == EntityType.DROWNED || key.getType() == EntityType.HUSK || key.getType() == EntityType.ZOMBIE || key.getType() == EntityType.ZOMBIFIED_PIGLIN) {
                    //obtain zombie
                    zombie = (Zombie) key;
                    //add inventory
                    contents.addAll(Arrays.asList(zombie.getEquipment().getArmorContents()));
                    contents.add(zombie.getEquipment().getItemInMainHand());
                    contents.add(zombie.getEquipment().getItemInOffHand());
                }
                //check for zombie villager
                else if (key.getType() == EntityType.ZOMBIE_VILLAGER) {
                    //obtain zombie villager
                    zvillager = (ZombieVillager) key;
                    //obtain inventory
                    contents.addAll(Arrays.asList(zvillager.getEquipment().getArmorContents()));
                    contents.add(zvillager.getEquipment().getItemInMainHand());
                    contents.add(zvillager.getEquipment().getItemInOffHand());
                    //obtain villager data
                    entityData = entityData.concat(zvillager.getVillagerProfession().toString() + "¥");
                    entityData = entityData.concat(zvillager.getVillagerType().toString() + "¥");
                }
                //check for contents
                if (!contents.isEmpty()) {
                    //get from inventories
                    contentString = MissingBukkitMethods.convertFromInventory(contents);
                }
                //check for trades
                if (trades != null) {
                    //cycle through trades
                    for (MerchantRecipe keys : trades) {
                        //get trade
                        trade = keys.getIngredients();
                        //add the result
                        trade.add(keys.getResult());
                        //add inv contents
                        contentString = contentString.concat(MissingBukkitMethods.convertFromInventory(trade));
                        //add uses and demand
                        contentString = contentString.concat("£" + keys.getMaxUses() + "£" + keys.getDemand() + "€");
                    }
                }
                //add entity
                saveentities.add(key.getType().toString() + ";{" + entityData + contentString + "};" + key.getLocation().getX() + ";" + key.getLocation().getY() + ";" + key.getLocation().getZ() + ";" + key.getLocation().getYaw() + ";" + key.getLocation().getPitch());
                entityData = "";
            }
        }
        //save file
        schematic.set("Blocks:", saveblocks);
        schematic.set("Entities:", saveentities);
        try {
            schematic.save(schem);
        }
        catch (IOException ex) {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT MAKE SCHEMATIC FOR ARENA " + this.name + '!');
            }
        }
    }
    /**
     *
     * This method regenerates the schematic of a given this.It's pretty optimized,  
     * but not recommended to reset after every match, especially if your arena is huge 
     * For reference, a 300x150x300 area of normal terrain 
     * takes 17 seconds to regenerate using an Intel i5-9300H 2.4 ghz CPU 
     * and uses 2.5 GB RAM to do so. That means it can regenerate up to 1,000,000 blocks per second.
     * ONLY reset if there are lots of block changes (you allow building/breaking).
     * It WILL CAUSE LAG with very big arenas and complex arenas (lots of different blocks). 
     *
     * @param useBlock replace blocks
     * @param useData replace blockdata
     * @param useMeta replace signs and containers
     * @param useEntity replace entities
     */
    public void loadArenaSchematic(boolean useBlock, boolean useData, boolean useMeta, boolean useEntity) {
        //file
        FileConfiguration schematic = FileManager.getCustomData(plugin, "region.arena", this.filePath);
        //load schematic list
        List<String> blocklist = schematic.getStringList("Blocks:");
        List<String> entitylist = schematic.getStringList("Entities:");
        //check if list is empty
        if (blocklist.isEmpty()) {
            //check for debug mode
            if (config.getBoolean("enable-debug-mode") == true) {
                //log error
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT LOAD ARENA SCHEMATIC " + this.name + '!');
            }
        }
        //local vars
        long newxl = 0;
        long newyl = 0;
        long newzl = 0;
        World worldd = Bukkit.getWorld(this.world);
        Material block = null;
        BlockData blockData = null;
        Sign sign = null;
        String[] signText = new String[3];
        String[] lastSignText = new String[3];
        String[] itemStorage = new String[55];
        String[] lastItemStorage = new String[55];
        int currentIndex = 1;
        String tempBlock = null;
        String lastBlock = "AIR";
        String lastBlockData = null;
        String[] tempBlockArray = new String[5];
        String[] tempArray = new String[2];
        List<String> currentBlockArray = new ArrayList<>();
        String[] tempEntity = new String[8];
        String[] tempEntityData = new String[5];
        String[] tempEntityStorage = new String[55];
        String[] tempTrades = new String[3];
        String[] tempTrade = new String[3];
        Entity entity = null;
        ArmorStand astand = null;
        ItemFrame frame = null;
        StorageMinecart cart = null;
        ItemStack[] cartstorage = new ItemStack[27];
        Painting paint = null;
        Location painting = null;
        Piglin piglin = null;
        PiglinBrute brute = null;
        Skeleton skeleton = null;
        Stray stray = null;
        Villager villager = null;
        WanderingTrader trader = null;
        MerchantRecipe trade = null;
        List<MerchantRecipe> trades = new ArrayList<>();
        Zombie zombie = null;
        ZombieVillager zvillager = null;
        int[] center = new int[3];
        //cycle x coords
        for (int xl = this.pos1[0]; xl <= this.pos2[0]; xl++) {
            //cycle y coords
            for (int yl = this.pos1[1]; yl <= this.pos2[1]; yl++) {
                //cycle z coords
                for (int zl = this.pos1[2]; zl <= this.pos2[2]; zl++) {
                    //reset temporary values
                    tempBlock = null;
                    tempBlockArray = new String[5];
                    tempArray = new String[2];
                    signText = new String[3];
                    itemStorage = new String[55];
                    currentBlockArray = new ArrayList<>();
                    //check if we're at the end of the file
                    if (currentIndex < blocklist.size()) {
                        //obtain string
                        tempBlock = blocklist.get(currentIndex);
                    }
                    //run if we're at the end
                    else {
                        //reset loop vars
                        xl = this.pos2[0] + 1;
                        yl = this.pos2[1] + 1;
                        zl = this.pos2[2] + 1;
                        //grab final string
                        tempBlock = blocklist.get(currentIndex-1);
                    }
                    //seperate sting into different parts
                    tempBlockArray = tempBlock.split(";",-1);
                    //split block and block data
                    tempArray = tempBlockArray[0].split("\\[",-1);
                    //add block to array
                    currentBlockArray.add(tempArray[0]);
                    //check if there is blockdata
                    if (tempArray.length > 1) {
                        //add block data to array
                        currentBlockArray.add(tempArray[1]);
                        //delete last bracket for later
                        currentBlockArray.set(1, tempArray[1].replace("]", ""));
                    }
                    //run if there's no blockdata
                    else {
                        //set placeholder for later
                        currentBlockArray.add("none");
                    }
                    //add meta valuse
                    currentBlockArray.add(tempBlockArray[1]);
                    //add position values
                    currentBlockArray.add(tempBlockArray[2]);
                    currentBlockArray.add(tempBlockArray[3]);
                    currentBlockArray.add(tempBlockArray[4]);
                    //check for invalid numbers, reset to loop pos if invalid
                    try {
                        newxl = (int) Long.parseLong(currentBlockArray.get(3));
                    }
                    catch (NumberFormatException ex) {
                        newxl = xl; 
                    }
                    try {
                        newyl = (int) Long.parseLong(currentBlockArray.get(4));
                    }
                    catch (NumberFormatException ex) {
                        newyl = yl; 
                    }
                    try {
                        newzl = (int) Long.parseLong(currentBlockArray.get(5));
                    }
                    catch (NumberFormatException ex) {
                        newzl = zl; 
                    }
                    //check if loop is equal to the current block position
                    if (xl == newxl && yl == newyl && zl == newzl) {
                        //check if using blocks
                        if (useBlock == true) {
                            //set block
                            block = Material.getMaterial(currentBlockArray.get(0).toUpperCase());
                            //save block for later
                            lastBlock = currentBlockArray.get(0).toUpperCase(); 
                        }
                        //delete block vars
                        else {
                            block = null;
                            lastBlock = "";
                        }
                        //check again if block has blockdata
                        if (!currentBlockArray.get(1).equals("none") && useData == true) {
                            //set blockdata
                            blockData = Material.getMaterial(currentBlockArray.get(0).toUpperCase()).createBlockData('[' + currentBlockArray.get(1) + ']');
                            //save blockdata for later
                            lastBlockData = currentBlockArray.get(1);
                        }
                        //delete blockdata vars
                        else {
                            blockData = null;
                            lastBlockData = "";
                        }
                        //check if block has signdata
                        if (!"".equals(currentBlockArray.get(2)) && currentBlockArray.get(0).contains("sign") && useMeta == true) {
                            //obtain sign data
                            signText = currentBlockArray.get(2).split("§",-1);
                            lastSignText = signText;
                        }
                        //delete sign vars
                        else {
                            signText = new String[3];
                            lastSignText = new String[3];
                        }
                        //check if blokc has items
                        if (!"".equals(currentBlockArray.get(2)) && !(currentBlockArray.get(0).contains("sign")) && useMeta == true) {
                            //obtain items
                            itemStorage = currentBlockArray.get(2).split("￠",-1);
                            lastItemStorage = itemStorage;
                        }
                        //delete item data
                        else {
                            itemStorage = new String[55];
                            lastItemStorage = new String[55];
                        }
                        //increment index
                        currentIndex++;
                    }
                    //run if loop is not equal to current block
                    else {
                        //check if using blocks
                        if (useBlock == true) {
                            //set block from previous loop
                            block = Material.getMaterial(lastBlock);
                        }
                        //check if there's blockdata
                        if (lastBlockData != null && Material.getMaterial(lastBlock) != Material.AIR && useData == true) {
                            //set blockdata
                            blockData = Material.getMaterial(lastBlock).createBlockData('[' + lastBlockData + ']');
                        }
                        //check if there's sign data
                        if (lastSignText[0] != null && useMeta == true) {
                            //set sign data
                            signText = lastSignText;
                        }
                        //check if there's item data
                        if (lastItemStorage[0] != null && useMeta == true) {
                            //set item data
                            itemStorage = lastItemStorage;
                        }
                    }
                    //check if block is somehow null to prevent errors
                    if (block == null) {
                        //set block to air
                        block = Material.AIR;
                    }
                    //check if using blocks and the block needs changing
                    if (useBlock == true && worldd.getBlockAt(xl, yl, zl).getType() != block) {
                        //change block in the world
                        worldd.getBlockAt(xl, yl, zl).setType(block);
                    }
                    //check if there's blockdata,if the blockdata needs to change and if blockdata is being used
                    if (blockData != null && worldd.getBlockAt(xl, yl, zl).getBlockData() != blockData && useData == true) {
                        //change blockdata in world
                        worldd.getBlockAt(xl, yl, zl).setBlockData(blockData);
                    }
                    //check if there's signdata, the block is a sign and meta is being used
                    if (signText[0] != null && !(worldd.getBlockAt(xl, yl, zl).getType().toString().equalsIgnoreCase("sign")) && useMeta == true) {
                        //obtain sign
                        sign = (Sign) worldd.getBlockAt(xl, yl, zl).getState();
                        //set lines
                        sign.setLine(0, signText[0].substring(1));
                        sign.setLine(1, signText[1]);
                        sign.setLine(2, signText[2]);
                        sign.setLine(3, signText[3].substring(0, signText[3].length() - 1));
                        sign.update(true);
                    }
                    //check if there's item data, the block isnt a sign and meta is being used
                    if (!"".equals(itemStorage[0]) && !"".equals(currentBlockArray.get(2)) && !(currentBlockArray.get(0).contains("sign")) && useMeta == true) {
                        //set inventory
                        MissingBukkitMethods.convertToInventory(itemStorage, true, worldd, block, xl, yl, zl);
                    }
                    //check if using blockdata
                    if (useData == true) {
                        //check if block has a vertical pair (doors, tall grass, etc.)
                        if (worldd.getBlockAt(xl, yl, zl).getBlockData().getAsString().contains("half=upper")) {
                            //set blockdata for other pair
                            if (!(currentBlockArray.get(0).contains("air"))) {
                                blockData = Material.getMaterial(currentBlockArray.get(0).toUpperCase()).createBlockData('[' + currentBlockArray.get(1).replace("half=upper", "half=lower") + ']');
                            }
                            //change block+blockdata for pair
                            worldd.getBlockAt(xl, yl-1, zl).setType(block);
                            if (!(currentBlockArray.get(0).contains("air"))) {
                                worldd.getBlockAt(xl, yl-1, zl).setBlockData(blockData);
                            }
                        }
                        //check if block has a horizontal pair(beds, etc.)
                        else if (worldd.getBlockAt(xl, yl, zl).getBlockData().getAsString().contains("part=head")) {
                            //set blockdata for other pair
                            blockData = Material.getMaterial(currentBlockArray.get(0).toUpperCase()).createBlockData('[' + currentBlockArray.get(1).replace("part=head", "part=foot") + ']');
                            //check if block is facing north
                            if (blockData.getAsString().contains("facing=north")) {
                                //change block+blockdata for pair
                                worldd.getBlockAt(xl, yl, zl+1).setType(block);
                                worldd.getBlockAt(xl, yl, zl+1).setBlockData(blockData);
                            }
                            //if previous check failed, check if block is facing south
                            else if (blockData.getAsString().contains("facing=south")) {
                                //change block+blockdata for pair
                                worldd.getBlockAt(xl, yl, zl-1).setType(block);
                                worldd.getBlockAt(xl, yl, zl-1).setBlockData(blockData);
                            }
                            //if previous check failed, check if block is facing west
                            else if (blockData.getAsString().contains("facing=west")) {
                                //change block+blockdata for pair
                                worldd.getBlockAt(xl+1, yl, zl).setType(block);
                                worldd.getBlockAt(xl+1, yl, zl).setBlockData(blockData);
                            }
                            //if previous check failed, check if block is facing east
                            else if (blockData.getAsString().contains("facing=east")) {
                                //change block+blockdata for pair
                                worldd.getBlockAt(xl-1, yl, zl).setType(block);
                                worldd.getBlockAt(xl-1, yl, zl).setBlockData(blockData);
                            }
                        }
                    }
                }
            }
        }
        //check if using entities
        if (useEntity == true) {
            //obtain center
            center[0] = (this.pos1[0] + this.pos2[0])/2;
            center[1] = (this.pos1[1] + this.pos2[1])/2;
            center[2] = (this.pos1[2] + this.pos2[2])/2;
            //cycle through entities in selection
            for (Entity key : worldd.getNearbyEntities(new Location(worldd, center[0], center[1], center[2]), this.pos2[0]-center[0], this.pos2[1]-center[1], this.pos2[2]-center[2])) {
                //check if entity isnt a player
                if (key.getType() != EntityType.PLAYER) {
                    //kill entity
                    key.remove();
                }
            }
            //cycle through entities
            for (String key : entitylist) {
                //obtain entity and entityData
                tempEntity = key.split(";", -1);
                tempEntityData = tempEntity[1].split("¥");
                //check if entity is not a painting
                if (EntityType.valueOf(tempEntity[0]) != EntityType.PAINTING) {
                    //summon entity
                    entity = worldd.spawnEntity(new Location(worldd, Double.parseDouble(tempEntity[2]), Double.parseDouble(tempEntity[3]), Double.parseDouble(tempEntity[4]), Float.parseFloat(tempEntity[5]), Float.parseFloat(tempEntity[6])), EntityType.valueOf(tempEntity[0]));
                }
                else {
                    //get painting location
                    painting = new Location(worldd, Double.parseDouble(tempEntity[2]), Double.parseDouble(tempEntity[3]), Double.parseDouble(tempEntity[4]), Float.parseFloat(tempEntity[5]), Float.parseFloat(tempEntity[6]));
                    //adjust y
                    painting.setY(painting.getY()-1);
                    //adjust z
                    if (painting.getYaw() == 90.0) {
                        painting.setZ(painting.getZ()-1);
                    }
                    //adjust z
                    else if (painting.getYaw() == -90.0) {
                        painting.setZ(painting.getZ()+1);
                    }
                    //adjust x
                    else if (painting.getYaw() == 0.0) {
                        painting.setX(painting.getX()+1);
                    }
                    //adjust x
                    else if (painting.getYaw() == -180.0) {
                        painting.setX(painting.getX()-1);
                    }
                    //summon entity
                    entity = worldd.spawnEntity(painting, EntityType.PAINTING);
                }
                //check fi entity is an armour stand
                if (entity.getType() == EntityType.ARMOR_STAND) {
                    //obtain stand and itemdata
                    astand = (ArmorStand) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //set inventory
                    astand.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    astand.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    astand.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check if entity is an item frame
                else if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME) {
                    //obtain frame and set item
                    frame = (ItemFrame) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    frame.setItem(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[0]);
                }
                //check if entity is a painting
                else if (entity.getType() == EntityType.PAINTING) {
                    //obtain painting and set art
                    paint = (Painting) entity;
                    paint.setArt(Art.getByName(tempEntityData[0].replace("{","")), true);
                }
                //check if entity is a chest cart
                else if (entity.getType() == EntityType.MINECART_CHEST) {
                    //obtain cart and inventory
                    cart = (StorageMinecart) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //cycle through items
                    for (byte i=0 ; i < MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0).length; i++) {
                        try {
                            //add to inventory
                            cartstorage[i] = MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[i];
                        }
                        catch (IndexOutOfBoundsException ex) {
                            break;
                        }
                    }
                    //set contents
                    cart.getInventory().setContents(cartstorage);
                }
                //check for piglin
                else if (entity.getType() == EntityType.PIGLIN) {
                    //obtain piglin and storage
                    piglin = (Piglin) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //set inventory
                    piglin.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    piglin.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    piglin.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check for brute
                else if (entity.getType() == EntityType.PIGLIN_BRUTE) {
                    //obtain brute and storage
                    brute = (PiglinBrute) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //set inventory
                    brute.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    brute.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    brute.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check for skeleton
                else if (entity.getType() == EntityType.SKELETON) {
                    //obtain skeleton and storage
                    skeleton = (Skeleton) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //set inventory
                    skeleton.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    skeleton.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    skeleton.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check for stray
                else if (entity.getType() == EntityType.STRAY) {
                    //obtain stray and storage
                    stray = (Stray) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //set inventory
                    stray.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    stray.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    stray.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check for zombie
                else if (entity.getType() == EntityType.DROWNED || entity.getType() == EntityType.HUSK || entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
                    //obtain zombie and storage
                    zombie = (Zombie) entity;
                    tempEntityStorage = tempEntityData[0].split("￠", -1);
                    //set inventory
                    zombie.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    zombie.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    zombie.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check for zombie villager
                else if (entity.getType() == EntityType.ZOMBIE_VILLAGER) {
                    //obtain zombie villager
                    zvillager = (ZombieVillager) entity;
                    //set villager data
                    zvillager.setVillagerProfession(Profession.valueOf(tempEntityData[0].replace("{", "")));
                    zvillager.setVillagerType(Type.valueOf(tempEntityData[1]));
                    tempEntityStorage = tempEntityData[2].split("￠", -1);
                    //set inventory
                    zvillager.getEquipment().setArmorContents(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0));
                    zvillager.getEquipment().setItemInMainHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[4]);
                    zvillager.getEquipment().setItemInOffHand(MissingBukkitMethods.convertToInventory(tempEntityStorage, false, null, null, 0, 0, 0)[5]);
                }
                //check for villager
                else if (entity.getType() == EntityType.VILLAGER) {
                    //obtain villager and trades
                    villager = (Villager) entity;
                    tempTrades = tempEntityData[4].split("€",-1);
                    trades = new ArrayList<>();
                    //set villager data
                    villager.setProfession(Profession.valueOf(tempEntityData[0].replace("{", "")));
                    villager.setVillagerType(Type.valueOf(tempEntityData[1]));
                    villager.setVillagerLevel(Integer.parseInt(tempEntityData[2]));
                    villager.setVillagerExperience(Integer.parseInt(tempEntityData[3]));
                    //cycle through trades
                    for (byte i=0; i < tempTrades.length-1; i++) {
                        //get trade
                        tempTrade = tempTrades[i].split("£",-1);
                        //check for 2nd item slot
                        if (MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0).length == 1) {
                            //set trade
                            trade = new MerchantRecipe(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)[1], 0);
                        }
                        //set result
                        else {
                            trade = new MerchantRecipe(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)[2], 0);
                        }
                        //check for item
                        if (MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0).length != 1) {
                            //set item
                            trade.setIngredients(Arrays.asList(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)).subList(0, 1));
                        }
                        else {
                            trade.setIngredients(Arrays.asList(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)));
                        }
                        try {
                            //set trade data
                            trade.setMaxUses(Integer.parseInt(tempTrade[1]));
                            trade.setDemand(Integer.parseInt(tempTrade[2]));
                        }
                        catch (IndexOutOfBoundsException ex) {}
                        //add trade
                        trades.add(trade);
                    }
                    //set trades
                    villager.setRecipes(trades);
                }
                //check for wandering trader
                else if (entity.getType() == EntityType.WANDERING_TRADER) {
                    //obtain trader and trades
                    trader = (WanderingTrader) entity;
                    tempTrades = tempEntityData[0].split("€",-1);
                    trades = new ArrayList<>();
                    //cycle trhough trades
                    for (byte i=0; i < tempTrades.length-1; i++) {
                        //obtain trade
                        tempTrade = tempTrades[i].split("£",-1);
                        //check for 2nd item slot
                        if (MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0).length == 1) {
                            //get trade
                            trade = new MerchantRecipe(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)[1], 0);
                        }
                        else {
                            //get result
                            trade = new MerchantRecipe(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)[2], 0);
                        }
                        //check for item
                        if (MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0).length != 1) {
                            //set item
                            trade.setIngredients(Arrays.asList(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)).subList(0, 1));
                        }
                        else {
                            trade.setIngredients(Arrays.asList(MissingBukkitMethods.convertToInventory(tempTrade[0].split("￠", -1), false, null, null, 0, 0, 0)));
                        }
                        try {
                            //get trade data
                            trade.setMaxUses(Integer.parseInt(tempTrade[1]));
                            trade.setDemand(Integer.parseInt(tempTrade[2]));
                        }
                        catch (IndexOutOfBoundsException ex) {}
                        //add trade
                        trades.add(trade);
                    }
                    //set trades
                    trader.setRecipes(trades);
                }
            }
        }
    }
    /**
     *
     * This method checks if a given player is inside the arena. 
     *
     * @param player The player to check
     * @return true if player is in the arena
     */
    public boolean isInArena(Player player) {
        //check if in arena
        return !(player.getLocation().getX() < this.pos1[0] || player.getLocation().getX() > this.pos2[0] || player.getLocation().getY() < this.pos1[1] || player.getLocation().getY() > this.pos2[1] || player.getLocation().getZ() < this.pos1[2] || player.getLocation().getZ() > this.pos2[2]); //return if all checks fail
    }
    /**
     *
     * This method centerizes a specified location.
     * "Centerize" means rounding x+z to the nearest .5, y to .0, yaw to 90 degrees and pitch to 0
     * You can choose weather or not yaw and pitch are used. 
     *
     * @param loc the position array to centerize
     * @param affectRotation true if rotation should be checked
     * @return
     */
    public float[] centerizeLocation(double[] loc, boolean affectRotation) {
        //local vars
        float[] newLoc = new float[5];
        newLoc[0] = (float) loc[0];
        newLoc[1] = (float) loc[1];
        newLoc[2] = (float) loc[2];
        //check if x is negative
        if (newLoc[0] < 0) {
            //centerize x
            newLoc[0] = (int) newLoc[0];
            newLoc[0] -= 0.5;
        }
        //check if x is positive
        else if (newLoc[0] >= 0) {
            //centerize x
            newLoc[0] = (int) newLoc[0];
            newLoc[0] += 0.5;
        }
        //set y
        newLoc[1] = (int) newLoc[1];
        //check if z is negative
        if (newLoc[2] < 0) {
            //centerize z
            newLoc[2] = (int) newLoc[2];
            newLoc[2] -= 0.5;
        }
        //check if z is positive
        else if (newLoc[2] >= 0) {
            //centerize z
            newLoc[2] = (int) newLoc[2];
            newLoc[2] += 0.5;
        }
        if (affectRotation == true) {
            newLoc[3] = (float) loc[3];
            newLoc[4] = (float) loc[4];
            //check if yaw should face at 0 degrees
            if (newLoc[3] >= -45 && newLoc[3] < 45) {
                newLoc[3] = (float) 0.0;
            }
            //check if yaw should face at 90 degrees
            else if (newLoc[3] >= 45 && newLoc[3] < 135) {
                newLoc[3] = (float) 90.0;
            }
            //check if yaw should face at 180 degrees
            else if (newLoc[3] >= 135 || newLoc[3] < -135) {
                newLoc[3] = (float) -180.0;
            }
            //check if yaw should face at 270 degrees
            else if (newLoc[3] >= -135 && newLoc[3] < -45) {
                newLoc[3] = (float) -90.0;
            }
            //reset pitch
            newLoc[4] = (float) 0.0;
        }
        return newLoc;
    }
    /**
     * 
     * This method saves the settings.yml file
     * 
     */
    public void saveFiles() {
        //local vars
        File arenaDataf = new File(this.plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(this.plugin, "settings", this.filePath);
        byte indexx = 0;
        String name = "";
        //set file
        //pos1 and 2
        arenaData.set("pos1.x", this.pos1[0]);
        arenaData.set("pos1.y", this.pos1[1]);
        arenaData.set("pos1.z", this.pos1[2]);
        arenaData.set("pos2.x", this.pos2[0]);
        arenaData.set("pos2.y", this.pos2[1]);
        arenaData.set("pos2.z", this.pos2[2]);
        //world + name
        arenaData.set("name", this.name);
        arenaData.set("world", this.world);
        //player counts
        arenaData.set("minPlayers", this.minPlayers);
        arenaData.set("skipPlayers", this.skipPlayers);
        arenaData.set("maxPlayers", this.maxPlayers);
        //spawnpoints
        for (Spawn key : this.spawns) {
            indexx++;
            if (!key.getName().equalsIgnoreCase(name)) {
                arenaData.set("Spawnpoints.Spawn" + indexx + ".name", key.getName());
                arenaData.set("Spawnpoints.Spawn" + indexx + ".x", key.getLocation()[0]);
                arenaData.set("Spawnpoints.Spawn" + indexx + ".y", key.getLocation()[1]);
                arenaData.set("Spawnpoints.Spawn" + indexx + ".z", key.getLocation()[2]);
                arenaData.set("Spawnpoints.Spawn" + indexx + ".yaw", key.getLocation()[3]);
                arenaData.set("Spawnpoints.Spawn" + indexx + ".pitch", key.getLocation()[4]);
            }
            else {
                this.spawns.remove(indexx);
            }
            name = key.getName();
        }
        //center
        arenaData.set("Center.x", this.centerPos[0]);
        arenaData.set("Center.y", this.centerPos[1]);
        arenaData.set("Center.z", this.centerPos[2]);
        arenaData.set("Center.yaw", this.centerPos[3]);
        arenaData.set("Center.pitch", this.centerPos[4]);
        //spec
        arenaData.set("Spec.x", this.specPos[0]);
        arenaData.set("Spec.y", this.specPos[1]);
        arenaData.set("Spec.z", this.specPos[2]);
        arenaData.set("Spec.yaw", this.specPos[3]);
        arenaData.set("Spec.pitch", this.specPos[4]);
        //lobby
        arenaData.set("Lobby.world", this.lobbyWorld);
        arenaData.set("Lobby.x", this.lobbyPos[0]);
        arenaData.set("Lobby.y", this.lobbyPos[1]);
        arenaData.set("Lobby.z", this.lobbyPos[2]);
        arenaData.set("Lobby.yaw", this.lobbyPos[3]);
        arenaData.set("Lobby.pitch", this.lobbyPos[4]);
        //exit
        arenaData.set("Exit.world", this.exitWorld);
        arenaData.set("Exit.x", this.exitPos[0]);
        arenaData.set("Exit.y", this.exitPos[1]);
        arenaData.set("Exit.z", this.exitPos[2]);
        arenaData.set("Exit.yaw", this.exitPos[3]);
        arenaData.set("Exit.pitch", this.exitPos[4]);
        //time vars
        arenaData.set("lobby-wait-time", this.lobbyWaitTime);
        arenaData.set("lobby-skip-time", this.lobbySkipTime);
        arenaData.set("game-wait-time", this.gameWaitTime);
        arenaData.set("game-length-time", this.gameLengthTime);
        //save file
        try {
            arenaData.save(arenaDataf);
        } 
        catch (IOException ex) {
            //check for debug mode
            if (config.getBoolean("enable-debug-mode") == true) {
                //log error
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SAVE SETTINGS.YML FOR ARENA " + this.name + '!');
            }
        }
    }
    /**
     * 
     * This method sets the permission for the selected arena
     * Usage of this is not recommended, as it does now register or unregister
     * any permissions, which is required for them to work
     * 
     * @param perm the permission to set
     */
    @Deprecated
    public void setPermission(Permission perm) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).arenaPerm = perm;
        }
        //set selection
        this.arenaPerm = perm;
    }
    /**
     * 
     * This method gets the permission for the selected arena.  
     * 
     * @return The permission object 
     */
    public Permission getPermission() {
        //reutrn instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).arenaPerm;
        }
        //return selection as a fallback
        return this.arenaPerm;
    }
    /**
     *
     * This method sets the min players of the selected arena. 
     *
     * @param minPlayerss the amount to set
     */
    public void setMinPlayers(byte minPlayerss) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).minPlayers = minPlayerss;
        }
        //set selection
        this.minPlayers = minPlayerss;
    }
    /**
     *
     * This method gets the min players of the selected arena
     *
     * @return the minPlayers amount
     */
    public byte getMinPlayers() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).minPlayers;
        }
        //return selection as a fallback
        return this.minPlayers;
    }
    /**
     *
     * This method sets the max players of the selected arena. 
     * 
     * @param skipPlayerss the amount to set
     */
    public void setSkipPlayers(byte skipPlayerss) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).skipPlayers = skipPlayerss;
        }
        //set selection
        this.skipPlayers = skipPlayerss;
    }
    /**
     *
     * This method gets the max players of the selected arena
     *
     * @return the skipPlayers amount
     */
    public byte getSkipPlayers() {
        //set instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).skipPlayers;
        }
        //set selection as a fallback
        return this.skipPlayers;
    }
    /**
     *
     * This method sets the min players of the selected arena. 
     *
     * @param maxPlayerss the amount to set
     */
    public void setMaxPlayers(byte maxPlayerss) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).maxPlayers = maxPlayerss;
        }
        //set selection
        this.maxPlayers = maxPlayerss;
    }
    /**
     *
     * This method gets the min players of the selected arena
     *
     * @return the maxPlayers amount
     */
    public byte getMaxPlayers() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).maxPlayers;
        }
        //return selection as a fallback
        return this.maxPlayers;
    }
    /**
     *
     * This method sets the current player count.
     *
     * @param teamSizee the amount to set
     */
    public void setTeamSize(byte teamSizee) {
        //check if currentPlayers is negative
        if (teamSizee < 1) {
            teamSizee = 1;
        }
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).teamSize = teamSizee;
        }
        //set selection
        this.teamSize = teamSizee;
    }
    /**
     *
     * This method gets the current player count.
     *
     * @return the currentPlayers amount
     */
    public byte getTeamSize() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).teamSize;
        }
        //return selection as a fallback
        return this.teamSize;
    }
    /**
     *
     * This method sets the current player count.
     *
     * @param currentPlayerss the amount to set
     */
    public void setCurrentPlayers(byte currentPlayerss) {
        //check if currentPLayers is negative
        if (currentPlayerss < 0) {
            currentPlayerss = 0;
        }
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).currentPlayers = currentPlayerss;
        }
        //set selection
        this.currentPlayers = currentPlayerss;
    }
    /**
     *
     * This method gets the current player count.
     *
     * @return the currentPlayers amount
     */
    public byte getCurrentPlayers() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).currentPlayers;
        }
        //return selection as a fallback
        return currentPlayers;
    }
    /**
     *
     * This method changes the name of the current arena.
     * It will prevent setting to null, which breaks selections
     *
     * @param namee the name to set
     */
    public void setName(String namee) {
        //check if name is null
        if (namee != null) {
            //set instance list
            if (arenas.contains(this)) {
                arenas.get(this.index).name = namee;
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
     * This method gets the current arena's name. 
     *
     * @return the arena name
     */
    public String getName() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).name;
        }
        //return selection as a fallback
        return this.name;
    }
    /**
     *
     * This method sets the pos1 of the current arena. 
     *
     * @param poss1 the position array to set
     */
    public void setPos1(int[] poss1) {
        //set instance array
        if (arenas.contains(this)) {
            arenas.get(this.index).pos1 = poss1;
        }
        //set selection
        this.pos1 = poss1;
    }
    /**
     *
     * This method gets the current pos1 of the selected arena. 
     *
     * @return the position array
     */
    public int[] getPos1() {
        //return instance array
        if (arenas.contains(this)) {
            return arenas.get(this.index).pos1;
        }
        //return selection as a fallback
        return this.pos1;
    }
    /**
     *
     * This method sets the pos1 of the current arena.
     *
     * @param poss2
     */
    public void setPos2(int[] poss2) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).pos2 = poss2;
        }
        //set selection
        this.pos2 = poss2;
    }
    /**
     *
     * This method gets the current pos2 of the selected arena. 
     *
     * @return the position array
     */
    public int[] getPos2() {
        //return instance array
        if (arenas.contains(this)) {
            return arenas.get(this.index).pos2;
        }
        //return selection as a fallback
        return this.pos2;
    }
    /**
     *
     * This method sets the world of the selected arena. 
     *
     * @param worldd The world name to set
     */
    public void setWorld(String worldd) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).world = worldd;
        }
        //set selection
        this.world = worldd;
    }
    /**
     *
     * This method gets the world of the selected arena.
     * If you need to world object you need to get that yourself with this name
     * 
     * @return the world name
     */
    public String getWorld() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).world;
        }
        //return selection as a fallback
        return this.world;
    }
    /**
     *
     * This method gets the current file path.
     *
     * @param filePathh the filepath to set to starting from plugins/(pluginname)/
     */
    public void setFilePath(String filePathh) {
        if (this.plugin != null) {
            //copy directory to new name's directory
            new File(this.plugin.getDataFolder() + this.filePath).renameTo(new File(this.plugin.getDataFolder() + filePathh));
            //delete old directory
            new File(this.plugin.getDataFolder() + this.filePath).delete();
        }
        else {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT MOVE FILES BECAUSE THE PLUGIN IS NULL!");
        }
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).filePath = filePathh;
        }
        //set selection
        this.filePath = filePathh;
    }
    /**
     *
     * This method gets the current file path.
     *
     * @return the filepath starting from plugins/(pluginname)
     */
    public String getFilePath() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).filePath;
        }
        //return selection as a fallback
        return this.filePath;
    }
    /**
     *
     * This method sets the current amount of spawns in the selected this. 
     * It does not manage spawns in any way, so it should never change by 
     * more or less than one.
     *
     * @param currentSpawnn the amount to set to
     */
    public void setCurrentSpawn(int currentSpawnn) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).currentSpawn = currentSpawnn;
        }
        //set selection
        this.currentSpawn = currentSpawnn;
    }
    /**
     *
     * This method gets the amount of spawns loaded in the selected this. 
     * It does not get any spawn data, just the amount. 
     *
     * @return the currentSpawn amount
     */
    public int getCurrentSpawn() {
        //return instance array
        if (arenas.contains(this)) {
            return arenas.get(this.index).currentSpawn;
        }
        //return selection as a fallback
        return this.currentSpawn;
    }
    /**
     *
     * This method adds a spawn to the selected arena. 
     *
     * @param spawnn the spawn object to add
     */
    public void addSpawn(Spawn spawnn) {
        //add instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns.add(spawnn);
        }
        //add selection
        this.spawns.add(spawnn);
        this.currentSpawn++;
    }
    /**
     *
     * This method deletes the given spawn from the selected arena. 
     *
     * @param spawnn the spawn object to remove
     */
    public void removeSpawn(Spawn spawnn) {
        //remove instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns.remove(spawnn);
        }
        //remove selection
        this.spawns.remove(spawnn);
        this.currentSpawn--;
    }
    /**
     *
     * This method sets the spawns for the selected arena. 
     *
     * @param spawnss the list to set
     */
    public void setSpawns(List<Spawn> spawnss) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns = spawnss;
        }
        //set selection
        this.spawns = spawnss;
    }
    /**
     *
     * This method gets all the spawns from the selected arena
     *
     * @return the spawn list
     */
    public List<Spawn> getSpawns() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).spawns;
        }
        //return selection as a fallback
        return this.spawns;
    }
    /**
     *
     * This method adds a spawn to the selected arena. 
     *
     * @param teamm the team object to add
     */
    public void addTeam(Team teamm) {
        //add instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).teams.add(teamm);
        }
        //add selection
        this.teams.add(teamm);
    }
    /**
     *
     * This method deletes the given spawn from the selected arena. 
     *
     * @param teamm the team object to remove
     */
    public void removeSpawn(Team teamm) {
        //remove instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).teams.remove(teamm);
        }
        //remove selection
        this.teams.remove(teamm);
    }
    /**
     *
     * This method sets the spawns for the selected arena. 
     *
     * @param teamss the list to set
     */
    public void setTeams(List<Team> teamss) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).teams = teams;
        }
        //set selection
        this.teams = teamss;
    }
    /**
     *
     * This method gets all the spawns from the selected arena
     *
     * @return the team list
     */
    public List<Team> getTeams() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).teams;
        }
        //return selection as a fallback
        return this.teams;
    }
    /**
     *
     * This method sets the current lobby position of the selected arena. 
     *
     * @param lobby the position array to set
     */
    public void setLobby(double[] lobby) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).lobbyPos = lobbyPos;
        }
        //set selection
        this.lobbyPos = lobby;
    }
    /**
     *
     * This method obtains the current lobby position of the selected arena. 
     *
     * @return the position array 
     */
    public double[] getLobby() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbyPos;
        }
        //return selection as a fallback
        return this.lobbyPos;
    }
    /**
     *
     * This method sets the lobbyWorld for the selected arena. 
     *
     * @param worldd the world name to set
     */
    public void setLobbyWorld(String worldd) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).lobbyWorld = worldd;
        }
        //set selection
        this.lobbyWorld = worldd;
    }
    /**
     *
     * This method gets the lobbyWorld for the selected arena. 
     *
     * @return the world name
     */
    public String getLobbyWorld() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbyWorld;
        }
        //return selection as a fallback
        return this.lobbyWorld;
    }
    /**
     *
     * This method sets the current exit position of the selected arena. 
     *
     * @param exit the position array to set
     */
    public void setExit(double[] exit) {
        //set instance array
        if (arenas.contains(this)) {
            arenas.get(this.index).exitPos = exit;
        }
        //set selection
        this.exitPos = exit;
    }
    /**
     *
     * This method obtains the current exit position of the selected arena. 
     *
     * @return the position array
     */
    public double[] getExit() {
        //obtain exitPos
        if (arenas.contains(this)) {
            return arenas.get(this.index).exitPos;
        }
        return this.exitPos;
    }
    /**
    *
    * This method sets the exitWorld for the selected arena. 
    *
    * @param worldd the world name to set
    */
    public void setExitWorld(String worldd) {
        //set instance array
        if (arenas.contains(this)) {
            arenas.get(this.index).exitWorld = worldd;
        }
        //set selection
        this.exitWorld = worldd;
    }
    /**
     *
     * This method gets the exitWorld for the selected arena. 
     *
     * @return the world name
     */
    public String getExitWorld() {
        //return instance array
        if (arenas.contains(this)) {
            return arenas.get(this.index).exitWorld;
        }
        //return selection as a fallback
        return this.exitWorld;
    }
    /**
     *
     * This method sets the current spectator position of the selected arena. 
     *
     * @param spec the position array
     */
    public void setSpectatorPos(double[] spec) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).specPos = specPos;
        }
        //set selection
        this.specPos = spec;
    }
    /**
     *
     * This method obtains the current spectator position of the selected arena. 
     *
     * @return the position array
     */
    public double[] getSpectatorPos() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).specPos;
        }
        //return selection as a fallback
        return this.specPos;
    }
    /**
     *
     * This method sets the current center position of the selected arena. 
     *
     * @param center the position array to set
     */
    public void setCenter(double[] center) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).centerPos = center;
        }
        //set selection
        this.centerPos = center;
    }
    /**
     *
     * This method obtains the current center position of the selected arena. 
     *
     * @return the position array
     */
    public double[] getCenter() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).centerPos;
        }
        //return selection as a fallback
        return this.centerPos;
    }
    /**
     * 
     * This method sets the status of the selected arena.  
     * 
     * @param statuss THe status to set
     */
    public void setStatus(ArenaStatus statuss) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).status = statuss;
        }
        //set selection
        this.status = statuss;
    }
    /**
     *
     * This method obtains the current status of the selected arena. 
     *
     * @return the status object
     */
    public ArenaStatus getStatus() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).status;
        }
        //return selection as a fallback
        return this.status;
    }
    /**
     *
     * This method sets the wait time for the selected arena. 
     *
     * @param waitTimee the amount to set
     */
    public void setLobbyWaitTime(int waitTimee) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).lobbyWaitTime = waitTimee;
        }
        //set selection
        this.lobbyWaitTime = waitTimee;
    }
    /**
     *
     * This method gets the wait time for the selected arena. 
     *
     * @return the time amount
     */
    public int getLobbyWaitTime() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbyWaitTime;
        }
        //return selection as a fallback
        return this.lobbyWaitTime;
    }
    /**
     *
     * This method sets the skip time for the selected arena. 
     * 
     * @param waitTimee the amount to set
     */
    public void setLobbySkipTime(int waitTimee) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).lobbySkipTime = waitTimee;
        }
        //set selection
        this.lobbySkipTime = waitTimee;
    }
    /**
     *
     * This method gets the skip time for the selected arena. 
     *
     * @return the time amount
     */
    public int getLobbySkipTime() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbySkipTime;
        }
        //return selection as a fallback
        return this.lobbySkipTime;
    }
    /**
     *
     * This method sets the wait time for the selected arena. 
     *
     * @param waitTimee the amount to set
     */
    public void setGameWaitTime(int waitTimee) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).gameWaitTime = waitTimee;
        }
        //set selection
        this.gameWaitTime = waitTimee;
    }
    /**
     *
     * This method gets the wait time for the selected arena. 
     *
     * @return the time amount
     */
    public int getGameWaitTime() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).gameWaitTime;
        }
        //return selection as a fallback
        return this.gameWaitTime;
    }
    /**
     *
     * This method sets the length time for the selected arena. 
     *
     * @param waitTimee the amount to set
     */
    public void setGameLengthTime(int waitTimee) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).gameLengthTime = waitTimee;
        }
        //set selection
        this.gameLengthTime = waitTimee;
    }
    /**
     *
     * This method gets the length time for the selected arena. 
     *
     * @return the time amount
     */
    public int getGameLengthTime() {
        //return instance list
        if (arenas.contains(this)) {
            return arenas.get(this.index).gameLengthTime;
        }
        //return selection as a fallback
        return this.gameLengthTime;
    }
    /**
     *
     * This method sets the plugin for the selected arena. 
     *
     * @param pluginn the plugin to set
     */
    public void setPlugin(Plugin pluginn) {
        //set instance list
        if (arenas.contains(this)) {
            arenas.get(this.index).plugin = pluginn;
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
        if (arenas.contains(this)) {
            return arenas.get(this.index).plugin;
        }
        //return selection as a fallback
        return this.plugin;
    }
}