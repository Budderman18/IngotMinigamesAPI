package com.budderman18.IngotMinigamesAPI.Core;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 *
 * This class features methods that are used in a variety of areas that don't exist in Bukkit.
 * Most revolve along converting strings into data and vice versa
 * Since they're original systems, i'm not suprised Bukkit doesnt feature them 1:1, but suprised
 * a sensible version isnt a thing yet
 * You shouldn't have to use these but they're available for use.
 * 
 */
public class MissingBukkitMethods {
    /**
     * 
     * This method converts from PotionData to PotionEffect
     * 
     * @param pd potion data
     * @param itemType the type of item
     * @return the pottion effect
     */
    public static PotionEffect getFromBasePotion(PotionData pd, String itemType) {
        //local vars
        String pdstring = pd.getType().toString();
        PotionEffectType effect = PotionEffectType.HERO_OF_THE_VILLAGE;
        int length = 0;
        int amplifier = 0;
        //check if extended
        if (pd.isExtended() == true) {
            //set lengthened
            pdstring = "long_" + pdstring;
        }
        //check if strengthened
        if (pd.isUpgraded() == true) {
            //set strengthened
            pdstring = "strong_" + pdstring;
        }
        //check if fire, invis, jump, night, speed, strength, or water
        if (pd.getType() == PotionType.FIRE_RESISTANCE || pd.getType() == PotionType.INVISIBILITY || pd.getType() == PotionType.JUMP || pd.getType() == PotionType.NIGHT_VISION || pd.getType() == PotionType.SPEED || pd.getType() == PotionType.STRENGTH || pd.getType() == PotionType.WATER_BREATHING) {
            //check for long
            if (pdstring.contains("long")) {
                length = 480;
            }
            else {
                length = 180;
            }
            //check for strong
            if (pdstring.contains("strong")) {
                length = 90;
                amplifier = 1;
            }
        }
        //check if poison or regen
        else if (pd.getType() == PotionType.POISON || pd.getType() == PotionType.REGEN) {
            //check for long
            if (pdstring.contains("long")) {
                length = 120;
            }
            else {
                length = 45;
            }
            //check for strong
            if (pdstring.contains("strong")) {
                length = 22;
                amplifier = 1;
            }
        }
        //check for slow, slowfall, or weakness
        else if (pd.getType() == PotionType.SLOWNESS || pd.getType() == PotionType.SLOW_FALLING || pd.getType() == PotionType.WEAKNESS) {
            //check for long
            if (pdstring.contains("long")) {
                length = 240;
            }
            else {
                length = 90;
            }
            //check for strong and not slowness
            if (pdstring.contains("strong") && pd.getType() != PotionType.SLOWNESS) {
                length = 45;
                amplifier = 1;
            }
            //check for strong and slowness
            else if (pdstring.contains("strong") && pd.getType() == PotionType.SLOWNESS) {
                length = 20;
                amplifier = 3;
            }
        }
        //check for turtle
        else if (pd.getType() == PotionType.TURTLE_MASTER) {
            //check for long
            if (pdstring.contains("long")) {
                length = 40;
            }
            else {
                length = 20;
            }
            //check for strong
            if (pdstring.contains("strong")) {
                amplifier = 1;
            }
        }
        //check for damage or heal
        else if (pd.getType() == PotionType.INSTANT_DAMAGE || pd.getType() == PotionType.INSTANT_HEAL) {
            //check for strong
            if (pdstring.contains("strong")) {
                amplifier = 1;
            }
        }
        //check if splash
        if (itemType.toLowerCase().contains("splash")) {
            //subtract 1/4th length
            length = (int) (length * 0.75);
        }
        //check if linger
        else if (itemType.toLowerCase().contains("linger")) {
            //subtract 3/4th length
            length = (int) (length * 0.25);
        }
        //check for arrow
        else if (itemType.toLowerCase().contains("arrow")) {
            //subtract 7/8th length
            length = (int) (length * 0.125);
        }
        //check if effect type isnt null
        if (pd.getType().getEffectType() != null) {
            //get type
            effect = pd.getType().getEffectType();
        }
        //return new effect
        return new PotionEffect(effect, length*20, amplifier, false, false);
    }
    /**
     * 
     * This method converts from PotionEffect to PotionData
     * 
     * @param effect the potion effect
     * @return the potion data
     */
    public static PotionData setToBasePotion(PotionEffect effect) {
        //local vars
        boolean longg = false;
        boolean strongg = false;
        //check for fire, invis, jump, night, speedstregth, water
        if (effect.getType() == PotionEffectType.FIRE_RESISTANCE || effect.getType() == PotionEffectType.INVISIBILITY || effect.getType() == PotionEffectType.JUMP || effect.getType() == PotionEffectType.NIGHT_VISION || effect.getType() == PotionEffectType.SPEED || effect.getType() == PotionEffectType.INCREASE_DAMAGE || effect.getType() == PotionEffectType.WATER_BREATHING) {
            //check if lengthened
            if (effect.getDuration()/20 == 480) {
                longg = true;
            }
        }
        //check for poison, regen
        else if (effect.getType() == PotionEffectType.POISON || effect.getType() == PotionEffectType.REGENERATION) {
            //check if lengthened
            if (effect.getDuration()/20 == 120) {
                longg = true;
            }
        }
        //check for turtle
        else if (effect.getType() == PotionEffectType.DAMAGE_RESISTANCE) {
            //check for long
            if (effect.getDuration()/20 == 40) {
                longg = true;
            }
        }
        //check for slow, slowfalling, weakness
        else if (effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.SLOW_FALLING || effect.getType() == PotionEffectType.WEAKNESS) {
            //check for long
            if (effect.getDuration()/20 == 240) {
                longg = true;
            }
        }
        //check if strengthened
        if (effect.getAmplifier() > 0) {
            strongg = true;
        }
        //return data
        return new PotionData(PotionType.getByEffect(effect.getType()), longg, strongg);
    }
    /**
     * 
     * This method converts from an ItemStack list to a string
     * 
     * @param contents the itemstack list
     * @return the string
     */
    public static String convertFromInventory(List<ItemStack> contents) {
        //local vars
        String contentString = "";
        String enchantString = " ";
        PotionMeta pmeta = null;
        FireworkMeta fmeta = null;
        String colors = "";
        String fades = "";
        String loreString = " ";
        byte indexx = 0;
        //cycle through inventory
        for (ItemStack key : contents) {
            //check if item exists
            if (key != null) {
                //check if item has effect
                if (key.getType().toString().toLowerCase().contains("potion") || key.getType().toString().toLowerCase().contains("tipped_arrow")) {
                    //obtain potion effects
                    pmeta = (PotionMeta) key.getItemMeta();
                    //add the base potion
                    pmeta.addCustomEffect(getFromBasePotion(pmeta.getBasePotionData(), key.getType().toString()), false);
                    //cycle through effects
                    for (PotionEffect keys : pmeta.getCustomEffects()) {
                        //make string
                        enchantString = enchantString.concat(keys.getType().getName() + '^' + keys.getDuration() + '^' + keys.getAmplifier() + '*');
                    }
                } 
                //check if item is firework
                else if (key.getType().toString().toLowerCase().contains("firework")) {
                    //get data
                    fmeta = (FireworkMeta) key.getItemMeta();
                    //cycle through effects
                    for (FireworkEffect keys : fmeta.getEffects()) {
                        //cycle through colors
                        for (Color color : keys.getColors()) {
                            //add colors
                            colors = colors.concat(Integer.toString(color.asRGB()));
                        }
                        //cycle though fades
                        for (Color color : keys.getFadeColors()) {
                            //add fade
                            fades = fades.concat(Integer.toString(color.asRGB()));
                        }
                        //create string
                        enchantString = enchantString.concat(keys.getType().toString() + '^' + colors + '^' + fades + '^' + keys.hasTrail() + '^' + keys.hasFlicker() + '^' + fmeta.getPower() + '*');
                    }
                } 
                else {
                    //obtain all enchantments
                    for (Enchantment keys : key.getEnchantments().keySet()) {
                        //obtain enchantment
                        enchantString = enchantString.concat(keys.getName() + '^' + key.getEnchantments().get(keys) + '*');
                    }
                }
                //check for itemmeta
                if (key.getItemMeta() != null) {
                    //check if lore exists
                    if (key.getItemMeta().getLore() != null) {
                        //cycle through lore
                        for (String keys : key.getItemMeta().getLore()) {
                            //add lore
                            loreString = loreString.concat(ChatColor.stripColor(keys) + '‰');
                        }
                    }
                }
                //check if valid material
                if (key.getType() != Material.AIR) {
                    //add item
                    contentString = contentString.concat("￠" + indexx + "§" + key.getType().name() + '§' + key.getAmount() + '§' + key.getDurability());
                    //check if item isnt null
                    if (key.getItemMeta() != null) {
                        //add meta string
                        contentString = contentString.concat('§' + enchantString + '§' + key.getItemMeta().getDisplayName() + '§' + loreString + '§' + key.getItemMeta().isUnbreakable());
                    }
                }
            }
            //reset vars
            indexx++;
            loreString = "";
            enchantString = "";
            colors = "";
            fades = "";
        }
        return contentString;
    }
    /**
     * 
     * This method converts from a string into an itemstack array
     * 
     * @param itemStorage the item string array
     * @param useLoopVars use the following parameters
     * @param worldd world to check
     * @param block block to check
     * @param xl x pos
     * @param yl y pos
     * @param zl z pos
     * @return the itemstack array
     */
    public static ItemStack[] convertToInventory(String[] itemStorage, boolean useLoopVars, World worldd, Material block, int xl, int yl, int zl) {
        //local vars
        Inventory inventory = null;
        ItemStack[] items = new ItemStack[56];
        ItemStack item = new ItemStack(Material.AIR);
        ItemMeta meta = item.getItemMeta();
        PotionMeta pmeta = (PotionMeta) item.getItemMeta();
        FireworkMeta fmeta = (FireworkMeta) item.getItemMeta();
        FireworkEffect.Builder firework =  FireworkEffect.builder();
        String[] storage = new String[8];
        String[] enchantString = new String[127];
        String[] levelString = new String[2];
        String[] potionString = new String[127];
        String[] effectString = new String[4];
        String[] fireworkString = new String[127];
        String[] workString = new String[7];
        List<String> loreString = new ArrayList<>();
        Chest chest = null;
        Furnace furnace = null;
        Dropper dropper = null;
        Hopper hopper = null;
        BrewingStand stand = null;
        //cycle through storage length
        for (byte i = 0; i < itemStorage.length; i++) {
            //check if out of array bounds
            try {
                if (itemStorage[i] != null);
            } 
            catch (IndexOutOfBoundsException ex) {
                break;
            }
            //check if storage isnt null
            if (itemStorage[i] != null) {
                //set new temparrays
                enchantString = new String[127];
                levelString = new String[2];
                potionString = new String[127];
                effectString = new String[4];
                fireworkString = new String[127];
                workString = new String[7];
                loreString = new ArrayList<>();
                storage = itemStorage[i].split("§", -1);
                storage[0] = storage[0].replace("{￠", "");
                storage[0] = storage[0].replace("￠", "");
                //check if sotrage is valid
                if (storage.length >= 7) {
                    try {
                        //check if there is metadata
                        if (!"".equals(storage[4])) {
                            //check if using potion
                            if (storage[1].toLowerCase().contains("potion") || storage[1].toLowerCase().contains("tipped")) {
                                //obtain potion data
                                potionString = storage[4].split("\\*", -1);
                                enchantString = new String[127];
                                fireworkString = new String[127];
                            } 
                            //check if using firework
                            else if (storage[1].toLowerCase().contains("firework")) {
                                //obtain firework data
                                fireworkString = storage[4].split("\\*", -1);
                                enchantString = new String[127];
                                potionString = new String[127];
                            } 
                            else {
                                //obtain enchant data
                                enchantString = storage[4].split("\\*", -1);
                                potionString = new String[127];
                                fireworkString = new String[127];
                            }
                        }
                        //check if there's lore
                        if (!"".equals(storage[6])) {
                            //cycle through all lore
                            for (byte j = 0; j < storage[6].split("‰", -1).length; j++) {
                                //add lore to string
                                loreString.add(ChatColor.translateAlternateColorCodes('&', storage[6].split("‰", -1)[j]));
                            }
                        }
                    } 
                    catch (IndexOutOfBoundsException | NullPointerException ex) {}
                    try {
                        //create item
                        item = new ItemStack(Material.getMaterial(storage[1].toUpperCase()));
                    } 
                    catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                        Bukkit.broadcastMessage("Tried setting material " + storage[1]);
                        item.setType(Material.AIR);
                    }
                    try {
                        //set amount
                        item.setAmount(Integer.parseInt(storage[2]));
                    } 
                    catch (IndexOutOfBoundsException ex) {
                        item.setAmount(1);
                    }
                    try {
                        //set durability
                        item.setDurability(Short.parseShort(storage[3]));
                    } 
                    catch (NumberFormatException | IndexOutOfBoundsException ex) {}
                    //obtain default meta
                    meta = item.getItemMeta();
                    //check for metadata
                    if (enchantString[0] != null && storage[4].length() > 0) {
                        //cycle through metastring
                        for (byte j = 0; j < enchantString.length - 1; j++) {
                            //cycle through levels
                            for (byte k = 0; k < enchantString[j].split("\\^", -1).length; k++) {
                                //add level
                                levelString[k] = enchantString[j].split("\\^", -1)[k];
                            }
                            //cycle through levels
                            for (byte k = 0; k < levelString.length; k++) {
                                //check if empty
                                if (levelString[k] == null) {
                                    //set empty string to prevent errors
                                    levelString[k] = "";
                                }
                            }
                            try {
                                //check if both strings have content
                                if (!levelString[0].equalsIgnoreCase("") && !levelString[1].equalsIgnoreCase("")) {
                                    //add enchantment
                                    meta.addEnchant(Enchantment.getByName(levelString[0].toUpperCase()), Integer.parseInt(levelString[1]), true);
                                }
                            } 
                            catch (IndexOutOfBoundsException | IllegalArgumentException | NullPointerException ex) {
                                Bukkit.broadcastMessage("Tried to apply enchant " + levelString[0]);
                                ex.printStackTrace();
                            }
                        }
                    } 
                    //check for potion meta
                    else if (potionString[0] != null && storage[4].length() > 0) {
                        //cycle through potion length
                        for (byte j = 0; j < potionString.length - 1; j++) {
                            //cycle through effects
                            for (byte k = 0; k < potionString[j].split("\\^", -1).length; k++) {
                                //get effects
                                effectString[k] = potionString[j].split("\\^", -1)[k];
                                //check if effect is null
                                if (effectString[k] == null) {
                                    //set to empty string to prevent errors
                                    effectString[k] = "";
                                }
                            }
                            try {
                                //get default mets
                                pmeta = (PotionMeta) item.getItemMeta();
                                //check for valid effect
                                if (!effectString[0].equalsIgnoreCase("") && !effectString[1].equalsIgnoreCase("") && !effectString[2].equalsIgnoreCase("")) {
                                    //add effect
                                    pmeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(effectString[0]), Integer.parseInt(effectString[1]) * 20, Integer.parseInt(effectString[2])), false);
                                }
                            } 
                            catch (ClassCastException | IndexOutOfBoundsException | NullPointerException | IllegalArgumentException ex) {}
                        }
                        try {
                            //get base effect and remove from list
                            pmeta.setBasePotionData(setToBasePotion(pmeta.getCustomEffects().get(0)));
                            pmeta.removeCustomEffect(pmeta.getCustomEffects().get(0).getType());
                            meta = pmeta;
                        } 
                        catch (NullPointerException | IndexOutOfBoundsException ex) {}
                    }
                    //check for firework meta
                    else if (fireworkString[0] != null && storage[4].length() > 0) {
                        //cycle through effect length
                        for (byte j = 0; j < fireworkString.length - 1; j++) {
                            //cycle through effect
                            for (byte k = 0; k < fireworkString[j].split("\\^", -1).length; k++) {
                                //get effect
                                workString[k] = fireworkString[j].split("\\^", -1)[k];
                                //check if effect is null
                                if (workString[k] == null) {
                                    //set empty string to prevent errors
                                    workString[k] = "";
                                }
                            }
                            try {
                                //check for validtype
                                if (!workString[0].equalsIgnoreCase("")) {
                                    //add type
                                    firework.with(FireworkEffect.Type.valueOf(workString[0]));
                                } 
                                else {
                                    firework.with(FireworkEffect.Type.BALL);
                                }
                                //check for valid color
                                if (!workString[1].equalsIgnoreCase("")) {
                                    //add color
                                    firework.withColor(Color.fromRGB(Integer.parseInt(workString[1])));
                                } 
                                else {
                                    firework.withColor(Color.WHITE);
                                }
                                //check for valid fade
                                if (!"".equals(workString[2])) {
                                    //add fade
                                    firework.withFade(Color.fromRGB(Integer.parseInt(workString[2])));
                                }
                                //check for trail
                                if (!workString[3].equalsIgnoreCase("")) {
                                    //set trail
                                    firework.trail(Boolean.parseBoolean(workString[3]));
                                }
                                //check for flicker
                                if (!workString[4].equalsIgnoreCase("")) {
                                    //set flicker
                                    firework.flicker(Boolean.parseBoolean(workString[4]));
                                }
                                //get default meta
                                fmeta = (FireworkMeta) item.getItemMeta();
                                //check for power
                                if (!workString[5].equalsIgnoreCase("")) {
                                    //set power
                                    fmeta.setPower(Integer.parseInt(workString[5]));
                                }
                                //add effect
                                fmeta.addEffect(firework.build());
                                firework = FireworkEffect.builder();
                            } 
                            catch (IllegalArgumentException | ClassCastException | IndexOutOfBoundsException | NullPointerException ex) {}
                            workString = new String[7];
                        }
                        meta = fmeta;
                    }
                } 
                try {
                    //set name
                    meta.setDisplayName(storage[5]);
                } 
                catch (IndexOutOfBoundsException | NullPointerException ex) {}
                try {
                    //set lore
                    meta.setLore(loreString);
                } 
                catch (NullPointerException ex) {}
                try {
                    //set unbreakable
                    meta.setUnbreakable(Boolean.parseBoolean(storage[7]));
                } 
                catch (IndexOutOfBoundsException | NullPointerException ex) {}
                //set itemmeta
                item.setItemMeta(meta);
                try {
                    //check if using loop
                    if (useLoopVars == true) {
                        block = worldd.getBlockAt(xl, yl, zl).getType();
                        inventory = null;
                        //check for chest or box
                        if (block == Material.CHEST || block == Material.TRAPPED_CHEST || block == Material.SHULKER_BOX) {
                            //set inventory
                            chest = (Chest) worldd.getBlockAt(xl, yl, zl).getState();
                            inventory = chest.getBlockInventory();
                        } 
                        //check for dispenser
                        else if (block == Material.DROPPER || block == Material.DISPENSER) {
                            //set inventory
                            dropper = (Dropper) worldd.getBlockAt(xl, yl, zl).getState();
                            inventory = dropper.getInventory();
                        } 
                        //check for furnace
                        else if (block == Material.FURNACE || block == Material.BLAST_FURNACE || block == Material.SMOKER) {
                            //set inventory
                            furnace = (Furnace) worldd.getBlockAt(xl, yl, zl).getState();
                            inventory = furnace.getInventory();
                        } 
                        //check for stand
                        else if (block == Material.BREWING_STAND) {
                            //set inventory
                            stand = (BrewingStand) worldd.getBlockAt(xl, yl, zl).getState();
                            inventory = stand.getInventory();
                        } 
                        //check for hopper
                        else if (block == Material.HOPPER) {
                            //set inventory
                            hopper = (Hopper) worldd.getBlockAt(xl, yl, zl).getState();
                            inventory = hopper.getInventory();
                        }
                        //check if inventory isnt null
                        if (inventory != null) {
                            try {
                                //set item
                                inventory.setItem(Integer.parseInt(storage[0]), item);
                            }
                            catch (NumberFormatException ex) {
                                inventory.setItem(0, item);
                            } 
                        }
                    }
                    else {
                        //set item
                        items[Integer.parseInt(storage[0])] = item;
                    }
                } 
                catch (NumberFormatException | IndexOutOfBoundsException ex) {}
            }
        }
        return items;
    }
}
