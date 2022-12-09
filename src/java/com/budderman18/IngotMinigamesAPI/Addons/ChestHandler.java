package com.budderman18.IngotMinigamesAPI.Addons;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.MissingBukkitMethods;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TimerHandler;
import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 *
 * This class handles chest filling, randomization and animation
 * 
 */
public class ChestHandler {
    /**
     * 
     * This method obtains all chests and both stores them and returns them
     * 
     * @param arena the arena to scan for
     * @return the chest list
     */
    public static List<Chest> chestsToRandomize(Arena arena) {
        //local vars
        List<Chest> chestss = new ArrayList<>();
        Chest chest = null;
        //cycle x coords
        for (int x = arena.getPos1()[0]; x <= arena.getPos2()[0]; x++) {
            //cycle y coords
            for (int y = arena.getPos1()[1]; y <= arena.getPos2()[1]; y++) {
                //cycle z coords
                for (int z = arena.getPos1()[2]; z <= arena.getPos2()[2]; z++) {
                    //check if block is a supported type
                    if (Bukkit.getWorld(arena.getWorld()).getBlockAt(x, y, z).getType() == Material.CHEST || Bukkit.getWorld(arena.getWorld()).getBlockAt(x, y, z).getType() == Material.TRAPPED_CHEST || Bukkit.getWorld(arena.getWorld()).getBlockAt(x, y, z).getType() == Material.BARREL || Bukkit.getWorld(arena.getWorld()).getBlockAt(x, y, z).getType() == Material.SHULKER_BOX) {
                        //get the chest
                        chest = (Chest) Bukkit.getWorld(arena.getWorld()).getBlockAt(x, y, z).getState();
                        chest.getLocation().getChunk().load();
                        //chest if chest has an item in  1st slot
                        if (chest.getBlockInventory().getItem(0) != null) {
                            //store the itemname as a lock for later
                            chest.setLock(chest.getBlockInventory().getItem(0).getType().name());
                            //add chest
                            chestss.add(chest);
                        }
                    }
                }
            }
        }
        return chestss;
    }
    /**
     * 
     * This method randomizes items and fills them into the desired chests
     * 
     * @param chests the chests to fill into
     * @param chestconfig the file to use
     */
    public static void fillChests(List<Chest> chests, FileConfiguration chestconfig) {
        //local vars
        List<String> tiers = new ArrayList<>();
        Material material = null;
        List<String> tempItems = new ArrayList<>();
        List<String> items = new ArrayList<>(100);
        byte minItems = 2;
        byte maxItems = 2;
        boolean useCategory = false;
        ItemStack item = null;
        byte amount = 1;
        byte index = 0;
        byte trueIndex = 0;
        short infLoop = 0;
        byte randNum = 0;
        boolean stopChecking = false;
        String currentItem = "";
        byte categorySize = 0;
        List<Byte> categories = new ArrayList<>();
        List<Byte> usedCategories = new ArrayList<>();
        List<Byte> usedSlots = new ArrayList<>();
        List<ItemStack> newItems = new ArrayList<>();
        List<ItemStack> invSize = new ArrayList<>();
        Random random = new Random();
        //cycle through file
        for (String key : chestconfig.getKeys(false)) {
            //check if not on version
            if (!key.contains("version")) {
                //add chest tier
                tiers.add(key);
            }
        }
        //cycle through chests
        for (Chest key : chests) {
            //reset stop
            stopChecking = false;
            //cycle through all tiers
            for (String keys : tiers) {
                //check if item 0 exists
                if (key.getBlockInventory().getItem(0) != null) {
                    //get item material
                    material = key.getBlockInventory().getItem(0).getType();
                    //check if material equals the tier's material
                    if (chestconfig.getString(keys + ".material").equalsIgnoreCase(material.toString())) {
                        //clear item list
                        items.clear();
                        //add blank item names so the spaces have something in them, which prevents issues when obtaining items
                        for (byte j = 0; j < 100; j++) {
                            items.add("");
                        }
                        //obtain tier vars
                        tempItems = chestconfig.getStringList(keys + ".items");
                        minItems = (byte) chestconfig.getInt(keys + ".minimum_items");
                        maxItems = (byte) chestconfig.getInt(keys + ".maximum_items");
                        //check if minItems is negative
                        if (minItems < 0) {
                            minItems = 0;
                        }
                        //check if maxItems is less than 1
                        if (maxItems < 1) {
                            maxItems = 1;
                        }
                        //reset indexes
                        index = 0;
                        trueIndex = 0;
                        //cycle through items
                        for (String keyss : tempItems) {
                            //keep running untill over its percent chance
                            while (index < Byte.parseByte(keyss.split("¥", -1)[1])) {
                                //add item
                                items.set(trueIndex, keyss);
                                index++;
                                trueIndex++;
                            }
                            //reset index
                            index = 0;
                        }
                        //obtain amount and useCategory
                        useCategory = chestconfig.getBoolean(keys + ".useCategory");
                        amount = (byte) random.nextInt(minItems, maxItems);
                        //reset infloop prevention
                        infLoop = 0;
                        //cycle until amount is reached
                        for (byte i = 0; i <= amount; i++) {
                            //obtain item chance
                            index = (byte) random.nextInt(0, 100);
                            //get item with matching percent
                            currentItem = items.get(index);
                            //check if using categories
                            if (useCategory == true) {
                                //cycle through items
                                for (String keyss : items) {
                                    //check if categories doesnt contain keyss's category
                                    if (!categories.contains(Byte.valueOf(keyss.split("¥",-1)[2]))) {
                                        //add category
                                        categories.add(Byte.valueOf(keyss.split("¥",-1)[2]));
                                    }
                                }
                                //obtain categorySize
                                categorySize = (byte) categories.size();
                                //run until usedCategorires is equal to total categories
                                while (usedCategories.size() != categorySize && infLoop < 32766) {
                                    //obtain item vars
                                    randNum = (byte) random.nextInt(0, categorySize+1);
                                    index = (byte) random.nextInt(0, items.size());
                                    currentItem = items.get(index);
                                    //check if all categories are used
                                    if (!usedCategories.contains(randNum) && currentItem.split("¥", -1)[2].equalsIgnoreCase(Byte.toString(randNum))) {
                                        //add category
                                        usedCategories.add(Byte.valueOf(currentItem.split("¥", -1)[2]));
                                        //obtain item
                                        currentItem = "￠0§".concat(currentItem.split("¥", -1)[0]);
                                        //convert item to be useable
                                        item = MissingBukkitMethods.convertToInventory(currentItem.split("¥", -1), false, null, null, 0, 0, 0)[0];
                                        //add item to list
                                        newItems.add(item);
                                        i++;
                                    }
                                    infLoop++;
                                }
                                //reset infloop prevetnion
                                infLoop = 0;
                                //check if item doesnt contain starting string
                                if (!currentItem.contains("￠0§")) {
                                    //add starting string
                                    currentItem = "￠0§".concat(currentItem.split("¥", -1)[0]);
                                    //convert item to be useable
                                    item = MissingBukkitMethods.convertToInventory(currentItem.split("¥", -1), false, null, null, 0, 0, 0)[0];
                                    //add to list
                                    newItems.add(item);
                                }
                            }
                            else {
                                //add starting string
                                currentItem = "￠0§".concat(currentItem);
                                //convert item to be useable
                                item = MissingBukkitMethods.convertToInventory(currentItem.split("¥", -1), false, null, null, 0, 0, 0)[0];
                                //add to list
                                newItems.add(item);
                            }
                            //clear lists
                            usedCategories.clear();
                            categories.clear();
                        }
                        break;
                    }
                }
            }
            //reset infloop prevetion
            infLoop = 0;
            //while still checking
            while (stopChecking == false && infLoop < 32766) {
                //clear chest
                key.getBlockInventory().clear();
                //cycle through item list
                for (ItemStack keys : newItems) {
                    //randomize position
                    randNum = (byte) random.nextInt(0, key.getBlockInventory().getSize());
                    //check if item is somehow null
                    if (keys != null) {
                        //check if desired slot is available
                        if (key.getBlockInventory().getItem(randNum) == null && !usedSlots.contains(randNum)) {
                            //add item
                            key.getBlockInventory().setItem(randNum, keys);
                            usedSlots.add(randNum);
                        }
                    }
                    //add invsize
                    invSize.add(keys);
                }
                //check if inventory is finished
                if (invSize.size() == newItems.size()) {
                    stopChecking = true;
                }
                infLoop++;
            }
            //clear lists
            newItems.clear();
            invSize.clear();
            usedSlots.clear();
        }
    }
    /**
     * 
     * This method resets the chest back to their "original form"
     * It actually only clears the chest and set back the tier material, as all
     * other contents are irrelevent for the filling process
     * 
     * @param chests chests to reset
     * @param chestconfig the file to read
     */
    public static void resetChests(List<Chest> chests, FileConfiguration chestconfig) {
        //local vars
        List<String> tiers = new ArrayList<>();
        Material material = null;
        //cycle through file
        for (String key : chestconfig.getKeys(false)) {
            //check if not on version
            if (!key.contains("version")){
                //add tier
                tiers.add(key);
            }
        }
        //cycle through chests
        for (Chest key : chests) {
            //load chest
            key.getLocation().getChunk().load();
            if (!"".equals(key.getLock())) {
                //get material store in chest lock
                material = Material.getMaterial(key.getLock());
                //clear inventory
                key.getBlockInventory().clear();
                //cycle through tiers
                for (String keys : tiers) {
                    //check if tier equals the chest's lock
                    if (chestconfig.getString(keys + ".material").equalsIgnoreCase(material.toString())) {
                        //set tier item
                        key.getBlockInventory().setItem(0, new ItemStack(material, 1));
                    }
                }
            }
        }
    }
    //use for the runnable (needed bc of stupid runnable limitations. Seriously why tf cant we use fields and vars that arent final?)
    private static Runnable getAction(Location chest, Zombie entityy, Horse riderr, Block[] blocks, BlockData[] data, short y, final byte fireworkIndex, FallingBlock blockk, ChestAnimation animation, Chest realChest, FileConfiguration chestconfig) {
        //local vars
        final List<Chest> chests = new ArrayList<>();
        //check if skyfall animation
        if (animation == ChestAnimation.SKYFALL) {
            return () -> {
                //runnable vars
                String itemname = "";
                FallingBlock block = null;
                Chest tempChest = null;
                //check if entity is alive
                if (entityy.isDead() == false) {
                    //move entity
                    entityy.setVelocity(new Vector(0,-0.015,0.6));
                    //check if at drop location
                    if (entityy.getLocation().getBlockZ() == chest.getBlockZ() && block == null) {
                        //spawn block
                        block = (FallingBlock) entityy.getWorld().spawnFallingBlock(entityy.getLocation(), Material.getMaterial(Material.OAK_LOG.toString()).createBlockData());
                        //centerize locations
                        block.getLocation().setX(block.getLocation().getX() + 0.5);
                        block.getLocation().setZ(block.getLocation().getZ() + 0.5);
                        realChest.getLocation().setX(block.getLocation().getX() + 0.5);
                        realChest.getLocation().setZ(block.getLocation().getZ() + 0.5);
                    }
                    //check if entity is ready to die
                    else if (entityy.getLocation().getBlockZ() >= chest.getBlockZ()+20) {
                        //kill entity
                        entityy.remove();
                        block = null;
                        //spawn explotion particles/sound
                        realChest.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, chest, 25);
                        realChest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                        //set block info
                        chest.getBlock().setType(realChest.getType());
                        chest.getBlock().setBlockData(realChest.getBlockData());
                        //obtain chest
                        tempChest = (Chest) chest.getBlock().getState();
                        chests.add(tempChest);
                        //first reset the chest to allow proper filling
                        resetChests(chests, chestconfig);
                        //get material
                        itemname = chestconfig.getString("SUPPLY_DROP.material").toUpperCase();
                        //check if item is null or empty
                        if (itemname == null || itemname == "") {
                            //use default (black wool)
                            itemname = "BLACK_WOOL";
                        }
                        //add item and fill chests
                        tempChest.getBlockInventory().setItem(0, new ItemStack(Material.getMaterial(itemname)));
                        fillChests(chests, chestconfig);
                    }
                    //run again
                    TimerHandler.runTimer(Main.getInstance(), 0, 2, getAction(chest, entityy, riderr, blocks, data, y, fireworkIndex, null, animation, realChest, chestconfig), true, false);
                }
            };
        }
        //check if using delivery
        if (animation == ChestAnimation.DELIVERY) {
            return () -> {
                //runnable vars
                Chest tempChest = null;
                String itemname = "";
                //check if entity is alive
                if (entityy.isDead() == false) {
                    //check if entity needs to jump
                    if (riderr.getLocation().getBlockY() < chest.getBlockY()) {
                        //raise elevation
                        riderr.setVelocity(new Vector(0,0.4,0.8));
                    }
                    else {
                        //lower elevation
                        riderr.setVelocity(new Vector(0,-0.8,0.8));
                    }
                    //check if at delivery spot
                    if (entityy.getLocation().getBlockZ() == chest.getBlockZ()) {
                        //set block
                        chest.getBlock().setType(Material.CHEST);
                    }
                    //check if ready to die
                    else if (entityy.getLocation().getBlockZ() >= chest.getBlockZ()+20) {
                        //remove entities
                        riderr.remove();
                        entityy.remove();
                        //set chest if it somehow didnt set
                        chest.getBlock().setType(Material.CHEST);
                        //obtain and add chest
                        tempChest = (Chest) chest.getBlock().getState();
                        chests.add(tempChest);
                        //reset chest
                        resetChests(chests, chestconfig);
                        //get material
                        itemname = chestconfig.getString("SUPPLY_DROP.material").toUpperCase();
                        //check if item is null or empty
                        if (itemname == null || itemname == "") {
                            //use default (black wool)
                            itemname = "BLACK_WOOL";
                        }
                        //add item and fill chest
                        tempChest.getBlockInventory().setItem(0, new ItemStack(Material.getMaterial(itemname)));
                        fillChests(chests, chestconfig);
                    }
                    //run again
                    TimerHandler.runTimer(Main.getInstance(), 0, 2, getAction(chest, entityy, riderr, blocks, data, y, fireworkIndex, null, animation, realChest, chestconfig), true, false);
                }
            };
        }
        //check if using beacon
        if (animation == ChestAnimation.BEACON) {
            return () -> {
                //runnable vars
                String itemname = "";
                byte index = 0;
                short trueY = y;
                Firework firework = null;
                FireworkMeta fmeta = null;
                Chest tempChest = null;
                //check if the chest is a beacon
                if (chest.getBlock().getType() == Material.BEACON) {
                    //check for western firework
                    if (fireworkIndex == 2 || fireworkIndex == 10) {
                        //spawn firework and get data
                        firework = (Firework) chest.getWorld().spawnEntity(new Location(chest.getWorld(), chest.getBlockX() + 1, chest.getBlockY(), chest.getBlockZ()), EntityType.FIREWORK);
                        fmeta = firework.getFireworkMeta();
                        //set power and add effect
                        fmeta.setPower(2);
                        fmeta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(Color.RED).withFade(Color.YELLOW).build());
                        //set data
                        firework.setFireworkMeta(fmeta);
                    }
                    //check for eastern firework
                    if (fireworkIndex == 4 || fireworkIndex == 10) {
                        //spawn firework and get data
                        firework = (Firework) chest.getWorld().spawnEntity(new Location(chest.getWorld(), chest.getBlockX(), chest.getBlockY(), chest.getBlockZ() + 1), EntityType.FIREWORK);
                        fmeta = firework.getFireworkMeta();
                        //set power and effect
                        fmeta.setPower(2);
                        fmeta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(Color.RED).withFade(Color.YELLOW).build());
                        //set data
                        firework.setFireworkMeta(fmeta);
                    }
                    //check for southern firework
                    if (fireworkIndex == 6 || fireworkIndex == 10) {
                        //spawn firework and get data
                        firework = (Firework) chest.getWorld().spawnEntity(new Location(chest.getWorld(), chest.getBlockX() - 1, chest.getBlockY(), chest.getBlockZ()), EntityType.FIREWORK);
                        fmeta = firework.getFireworkMeta();
                        //set power and effect
                        fmeta.setPower(2);
                        fmeta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(Color.RED).withFade(Color.YELLOW).build());
                        //set data
                        firework.setFireworkMeta(fmeta);
                    }
                    //check for northern firework
                    if (fireworkIndex == 8 || fireworkIndex == 10) {
                        //spawn firework and get data
                        firework = (Firework) chest.getWorld().spawnEntity(new Location(chest.getWorld(), chest.getBlockX(), chest.getBlockY(), chest.getBlockZ() - 1), EntityType.FIREWORK);
                        fmeta = firework.getFireworkMeta();
                        //set power and effect
                        fmeta.setPower(2);
                        fmeta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(Color.RED).withFade(Color.YELLOW).build());
                        //set data
                        firework.setFireworkMeta(fmeta);
                    }
                    //check if incrementing fireworkindex
                    if (fireworkIndex >= 0) {
                        //set to air
                        chest.getWorld().getBlockAt(chest.getBlockX(), chest.getBlockY() + trueY, chest.getBlockZ()).setType(Material.AIR);
                        //bump chest down 2 blocks
                        trueY-=2;
                        chest.getWorld().getBlockAt(chest.getBlockX(), chest.getBlockY() + trueY, chest.getBlockZ()).setType(Material.CHEST);
                        //spawn explosion particle and sound
                        chest.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, new Location(chest.getWorld(), chest.getBlockX(), chest.getBlockY() + trueY, chest.getBlockZ()), 25);
                        chest.getWorld().playSound(new Location(chest.getWorld(), chest.getBlockX(), chest.getBlockY() + trueY, chest.getBlockZ()), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                    }
                    //check if chest landed
                    if (trueY == 0) {
                        //spawn explostion particles and sound
                        chest.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, new Location(chest.getWorld(), chest.getBlockX(), chest.getBlockY(), chest.getBlockZ()), 25);
                        chest.getWorld().playSound(new Location(chest.getWorld(), chest.getBlockX(), chest.getBlockY() + trueY, chest.getBlockZ()), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                        //get beacon x poses
                        for (int x=chest.getBlockX()-1; x < chest.getBlockX()+2; x++) {
                            //get beacon z poses
                            for (int z=chest.getBlockZ() - 1; z < chest.getBlockZ() + 2; z++) {
                                //reset blocks
                                chest.getWorld().getBlockAt(x,chest.getBlockY() - 1,z).setType(blocks[index].getType());
                                chest.getWorld().getBlockAt(x,chest.getBlockY() - 1,z).setBlockData(data[index]);
                                index++;
                            }
                        }
                        //set back to chest
                        chest.getBlock().setType(Material.CHEST);
                        //obtain chest
                        tempChest = (Chest) chest.getBlock().getState();
                        chests.add(tempChest);
                        //reset chest
                        resetChests(chests, chestconfig);
                        //get material
                        itemname = chestconfig.getString("SUPPLY_DROP.material").toUpperCase();
                        //check if item is null or empty
                        if (itemname == null || itemname == "") {
                            //use default (black wool)
                            itemname = "BLACK_WOOL";
                        }
                        //add item and fill chests
                        tempChest.getBlockInventory().setItem(0, new ItemStack(Material.getMaterial(itemname)));
                        fillChests(chests, chestconfig);
                    }
                    //run again
                    TimerHandler.runTimer(Main.getInstance(), 0, 5, getAction(chest, entityy, riderr, blocks, data, trueY, (byte) (fireworkIndex+1), null, animation, realChest, chestconfig), true, false);
                }
            };
        }
        //check if using drill
        if (animation == ChestAnimation.DRILL) {
            return () -> { 
                //runnable vars
                String itemname = "";
                Chest tempChest = null;
                FallingBlock block = blockk;
                //check if just started
                if (fireworkIndex == 0) {
                    //set location
                    chest.setY(chest.getBlockY()-16);
                    //spawn particles and play sound
                    chest.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, chest, y);
                    chest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                }
                //check if at 7 time
                if (fireworkIndex == 7) {
                    //set location
                    chest.setY(chest.getBlockY()+8);
                    //spawn particles and play sound
                    chest.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, chest, y);
                    chest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                }
                //check at 13 time
                if (fireworkIndex == 13) {
                    //set location
                    chest.setY(chest.getBlockY()+4);
                    //spawn particles and play sound
                    chest.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, chest, y);
                    chest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                }
                //check at 15 time
                if (fireworkIndex == 15) {
                    //set location
                    chest.setY(chest.getBlockY()+2);
                    //spawn particles and play sound
                    chest.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, chest, y);
                    chest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                }
                //checkat 16 time
                if (fireworkIndex == 16) {
                    //set location
                    chest.setY(chest.getBlockY()+1);
                    //spawn particles and play sound
                    chest.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, chest, y);
                    chest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                }
                //check if emerged
                if (fireworkIndex >= 16) {
                    //check if block spawned
                    if (block == null) {
                        //spawn block and shoot up
                        block = (FallingBlock) chest.getWorld().spawnFallingBlock(chest.add(0.5, 0, 0.5), Material.getMaterial(Material.OAK_LOG.toString()).createBlockData());
                        block.setVelocity(new Vector(0,1,0));
                    }
                    //check if block is dead
                    if (block.isDead()) {
                        //set chest  and location location
                        chest.add(0, 1, 0);
                        chest.getBlock().setType(Material.CHEST);
                        //get and add chest 
                        tempChest = (Chest) chest.getBlock().getState();
                        chests.add(tempChest);
                        //reset chest
                        resetChests(chests, chestconfig);
                        //get material
                        itemname = chestconfig.getString("SUPPLY_DROP.material").toUpperCase();
                        //check if item is null or empty
                        if (itemname == null || itemname == "") {
                            //use default (black wool)
                            itemname = "BLACK_WOOL";
                        }
                        //add item and fill chest
                        tempChest.getBlockInventory().setItem(0, new ItemStack(Material.getMaterial(itemname)));
                        fillChests(chests, chestconfig);
                        //spawn particles and play sound
                        chest.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, chest, y);
                        chest.getWorld().playSound(chest, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                    }
                }
                //check if block is not null
                if (block != null) {
                    //run agaon if not dead
                    if (!block.isDead()) {
                        TimerHandler.runTimer(Main.getInstance(), 0, 3, getAction(chest, entityy, riderr, blocks, data, y, (byte) (fireworkIndex+1), block, animation, realChest, chestconfig), true, false);
                    }
                }
                else {
                    //run again
                    TimerHandler.runTimer(Main.getInstance(), 0, 3, getAction(chest, entityy, riderr, blocks, data, y, (byte) (fireworkIndex+1), block, animation, realChest, chestconfig), true, false);
                }
            };
        }
        return null;
    }
    /**
     * 
     * This method animated a given chest
     * 
     * @param chest the chest to animate
     * @param animation the animation to use
     * @param chestconfig the file to read from
     */
    public static void animateChest(Location chest, ChestAnimation animation, FileConfiguration chestconfig) {
        //local vars
        Runnable action = null;
        Zombie entity = null;
        Horse rider = null;
        Chest realChest = null;
        Location location = null;
        byte infLoop = 0;
        List<Chest> chests = new ArrayList<>();
        Block[] blocks = new Block[10];
        BlockData[] data = new BlockData[10];
        byte index = 0;
        //set to chest if not already
        chest.getWorld().getBlockAt(chest).setType(Material.CHEST);
        //check if using skyfall
        if (animation == ChestAnimation.SKYFALL) {
            //spawn and setup player
            entity = (Zombie) chest.getWorld().spawn(new Location(chest.getWorld(), chest.getX(), chest.getY() + 20, chest.getZ() - 20), Zombie.class);
            //entity = FakePlayer.createNPC(chest, "cargo plane + " + chest.getBlockZ());
            entity.setAware(false);
            entity.setAdult();
            entity.setGliding(true);
            entity.setInvulnerable(true);
            entity.getEquipment().setChestplate(new ItemStack(Material.ELYTRA, 1));
            entity.getEquipment().setItemInMainHand(new ItemStack(Material.CHEST, 1));
            entity.setCustomName("cargo plane|" + chest.getBlockZ());
            //obtain and reset chest
            realChest = (Chest) chest.getBlock().getState();
            chests.add(realChest);
            resetChests(chests, chestconfig);
            //call actions
            action = getAction(chest, entity, null, null, null, (short) 0, (byte) 0, null, animation, realChest, chestconfig);
            //set to air
            chest.getBlock().setType(Material.AIR);
            //run again
            TimerHandler.runTimer(Main.getInstance(), 0, 1, action, true, false);
        }
        //check if using delivery
        else if (animation == ChestAnimation.DELIVERY) {
            //spawn entities
            rider = (Horse) chest.getWorld().spawn(new Location(chest.getWorld(), chest.getX() + 1, chest.getY(), chest.getZ() - 20), Horse.class);
            //run while entity is not in a mobable area
            while (rider.getLocation().getBlock().getType() != Material.AIR && infLoop < 126) {
                //teleport mob up 1
                location = rider.getLocation();
                location.setY(location.getY()+1);
                rider.teleport(location);
                infLoop++;
            }
            //spawn entity and setup both
            entity = (Zombie) chest.getWorld().spawn(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ() - 20), Zombie.class);
            rider.addPassenger(entity);
            rider.setAware(false);
            rider.setAdult();
            rider.setGliding(true);
            rider.setInvulnerable(true);
            entity.setAware(false);
            entity.setAdult();
            entity.setGliding(true);
            entity.setInvulnerable(true);
            entity.getEquipment().setItemInMainHand(new ItemStack(Material.CHEST, 1));
            entity.setCustomName("Delivery Horse|" + chest.getBlockZ());
            //obtain and reset chest
            realChest = (Chest) chest.getBlock().getState();
            chests.add(realChest);
            resetChests(chests, chestconfig);
            //run action
            action = getAction(chest, entity, rider, null, null, (short) 0, (byte) 0, null, animation, realChest, chestconfig);
            //set chest to air
            chest.getBlock().setType(Material.AIR);
            //run again
            TimerHandler.runTimer(Main.getInstance(), 0, 1, action, true, false);
        }
        //check if using beacon
        else if (animation == ChestAnimation.BEACON) {
            //cycle x coords
            for (int x=chest.getBlockX()-1; x < chest.getBlockX()+2; x++) {
                //cycle z coords
                for (int z=chest.getBlockZ() - 1; z < chest.getBlockZ() + 2; z++) {
                    //obtain block data being changed
                    blocks[index] = chest.getWorld().getBlockAt(x, chest.getBlockY()-1, z);
                    data[index] = blocks[index].getBlockData();
                    //set block
                    blocks[index].setType(Material.DIAMOND_BLOCK);
                    index++;
                }
            }
            //obtain and reset chests
            realChest = (Chest) chest.getBlock().getState();
            chests.add(realChest);
            resetChests(chests, chestconfig);
            //run action
            action = getAction(chest, null, null, blocks, data, (short) 30, (byte) -8, null, animation, realChest, chestconfig);
            //set to beacon
            chest.getBlock().setType(Material.BEACON);
            chest.getWorld().getBlockAt(chest.getBlockX(), chest.getBlockY() + 30, chest.getBlockZ()).setType(Material.CHEST);
            //run again
            TimerHandler.runTimer(Main.getInstance(), 0, 5, action, true, false);
        }
        //check if using drill
        else if (animation == ChestAnimation.DRILL) {
            //obtain and reset chest
            realChest = (Chest) chest.getBlock().getState();
            chests.add(realChest);
            resetChests(chests, chestconfig);
            //run action
            action = getAction(chest, null, null, null, null, (short) 0, (byte) 0, null, animation, realChest, chestconfig);
            //set chest to air
            chest.getBlock().setType(Material.AIR);
            //run again
            TimerHandler.runTimer(Main.getInstance(), 0, 5, action, true, false);
        }
    }
    /**
     * 
     * This method obtains the file to use for chests
     * 
     * @param plugin the plugin to obtain the file from
     * @return The obtained file
     */
    public static FileConfiguration obtainChestFile(Plugin plugin) {
        return FileManager.getCustomData(plugin, "chest", "");
    }
}