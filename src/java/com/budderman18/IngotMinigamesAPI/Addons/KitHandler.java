package com.budderman18.IngotMinigamesAPI.Addons;

import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.MissingBukkitMethods;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * This class handles kit management
 * 
 */
public class KitHandler {
    /**
     * 
     * This method gets the items for a kit
     * 
     * @param kit the file to read from
     * @param name the name of the kit to use
     * @return the items
     */
    public static ItemStack[] getItems(FileConfiguration kit, String name) {
        //local vars
        ItemStack[] newKit = new ItemStack[41];
        String itemString = "￠";
        ItemStack[] tempItems = null;
        //cycle through item list
        for (String keys : kit.getStringList(name + ".items")) {
            //add item to string
            itemString = itemString.concat(keys + "￠");
        }
        //delete final symbol
        itemString = itemString.substring(0, itemString.length() - 1);
        //convert string to items
        tempItems = MissingBukkitMethods.convertToInventory(itemString.split("￠", -1), false, null, null, 0, 0, 0);
        //cycle through kit size
        for (byte i = 0; i < newKit.length; i++) {
            //set item
            newKit[i] = tempItems[i];
        }
        //return kit
        return newKit;
    }
    /**
     * 
     * This method gets the effects for a kit
     * 
     * @param kit the file to read from
     * @param name the name of the kit to use
     * @return the items
     */
    public static List<PotionEffect> getEffects(FileConfiguration kit, String name) {
        //local vars
        List<PotionEffect> effects = new ArrayList<>();
        //cycle through file
        for (String keys : kit.getStringList(name + ".effects")) {
            //add effect
            effects.add(new PotionEffect(PotionEffectType.getByName(keys.split("§", -1)[0]), Integer.parseInt(keys.split("§", -1)[1]), Integer.parseInt(keys.split("§", -1)[2])));
        }
        //return list
        return effects;
    }
    /**
     * 
     * This method obtains the file to use for kits
     * 
     * @param plugin the plugin to obtain the file from
     * @return The obtained file
     */
    public static FileConfiguration obtainKitFile(Plugin plugin) {
        //return file
        return FileManager.getCustomData(plugin, "kit", "");
    }
}