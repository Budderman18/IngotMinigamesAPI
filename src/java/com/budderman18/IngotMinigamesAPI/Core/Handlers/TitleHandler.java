package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * This class handles everything involving titles. 
 * 
 */
public class TitleHandler {
    /**
     * 
     * This constructor blocks new() usage as it does nothing
     * 
     */
    private TitleHandler() {}
    /**
     *
     * This method sets the title. 
     * Also changes subtitle. 
     * You can specify fades and lengths in it. 
     *
     * @param player The player to send to
     * @param title the title text
     * @param subtitle the subtitle text
     * @param fadeIn the time it takes to fade in in ticks
     * @param stay the time it will show in ticks
     * @param fadeOut the time it takes to fade out in ticks
     */
    public static void setTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        //send title
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }
    /**
     *
     * This method sets the action bar. 
     * You can't specify fades and lengths within action bars. 
     * This is a limitation that can't be worked around. 
     * 
     * @param player The player to send to
     * @param string the message to send
     */
    public static void setActionBar(Player player, String string) {
        //send actionbar
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', string)));
    }
}
