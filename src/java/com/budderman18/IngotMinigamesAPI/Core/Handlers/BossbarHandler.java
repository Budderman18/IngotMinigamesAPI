package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 *
 * This class handles everything dealing with bossbars. 
 * You can set title, color and style. 
 * 
 */
public class BossbarHandler {
    /**
     * 
     * This constructor blocks new() usage as it does nothingg
     * 
     */
    private BossbarHandler() {}
    /**
     *
     * This method sets the title of the current bossbar. It also adds the
     * player to the bossbar, since this should be ran before anything.
     *
     * @param player The player to affect
     * @param title the title text for the bar
     * @param bossbarr the bossbar object to use
     * @return the bossbar object
     */
    public static BossBar setBarTitle(Player player, String title, BossBar bossbarr) {
        //local vars
        BossBar newBossbar = Bukkit.createBossBar(null, BarColor.WHITE, BarStyle.SOLID);
        //check if bossbar is null
        if (bossbarr == null) {
            //setup bossbar new bossbar
            newBossbar.setTitle(ChatColor.translateAlternateColorCodes('&', title));
            newBossbar.addPlayer(player);
            return newBossbar;
        }
        else {
            //change existing bossbar
            bossbarr.setTitle(ChatColor.translateAlternateColorCodes('&', title));
            bossbarr.addPlayer(player);
            return bossbarr;
        }
    }
    /**
     *
     * This method changes the color of a certain boss color. It defaults to
     * white if you put in an invalid color.
     *
     * @param player the player to affect
     * @param color the color name to use
     * @param bossbarr thebosbar to affect
     */
    public static void setBarColor(Player player, String color, BossBar bossbarr) {
        //local vars
        BarColor bc = BarColor.WHITE;
        color = color.toUpperCase();
        //check for blue
        if (color.contains("BLUE")) {
            bc = BarColor.BLUE;
        }
        //check for green
        else if (color.contains("GREEN")) {
            bc = BarColor.GREEN;
        }
        //check for pink
        else if (color.contains("PINK")) {
            bc = BarColor.PINK;
        }
        //check for purple
        else if (color.contains("PURPLE")) {
            bc = BarColor.PURPLE;
        }
        //check for red
        else if (color.contains("RED")) {
            bc = BarColor.RED;
        }
        //check for yellow
        else if (color.contains("YELLOW")) {
            bc = BarColor.YELLOW;
        }
        //set color
        bossbarr.setColor(bc);
    }
    /**
     *
     * This method sets the progress/size of the bossbar.
     *
     * @param player the player to affect
     * @param size the since as a percentage decimal (0-1)
     * @param bossbarr the bossbar object
     */
    public static void setBarSize(Player player, double size, BossBar bossbarr) {
        bossbarr.setProgress(size);
    }
    /**
     *
     * This method changes the style of the bossbar. 
     * It defaults to solid if the input is invalid.
     *
     * @param style The style name to use
     * @param bossbarr the bossbar object 
     */
    public static void setBarStyle(String style, BossBar bossbarr) {
        //local vars
        BarStyle bs = BarStyle.SOLID;
        style = style.toUpperCase();
        //check for 6
        if (style.contains("SEGMENTED_6")) {
            bs = BarStyle.SEGMENTED_6;
        }
        //check for 10
        if (style.contains("SEGMENTED_10")) {
            bs = BarStyle.SEGMENTED_10;
        }
        //check for 12
        if (style.contains("SEGMENTED_12")) {
            bs = BarStyle.SEGMENTED_12;
        }
        //check for 20
        if (style.contains("SEGMENTED_20")) {
            bs = BarStyle.SEGMENTED_20;
        }
        //set style
        bossbarr.setStyle(bs);
    }
    /**
     *
     * This method removes a player's bossbar.
     *
     * @param player the player to affect
     * @param bossbarr the bossbar to remove
     */
    public static void clearBar(Player player, BossBar bossbarr) {
        bossbarr.removePlayer(player);
    }
    /**
     *
     * This method toggles weather a player can view the given bossbar.
     *
     * @param visable true to be visable
     * @param bossbarr the bossbar to affect
     */
    public static void displayBar(boolean visable, BossBar bossbarr) {
        bossbarr.setVisible(visable);
    }
}
