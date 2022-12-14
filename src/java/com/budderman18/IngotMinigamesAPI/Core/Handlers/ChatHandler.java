package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles everything involving chat
 * 
 */
public class ChatHandler {
    /**
     * 
     * This constructor blocks new() usage as it does nothing
     * 
     */
    private ChatHandler() {}
    /**
     *
     * This method changes messages into the universal format. 
     * Use this for every time a player chats that's playing. 
     *
     * @param player the player to send as (null for global)
     * @param message the message to use
     * @param format the format to use
     * @return the formatted message
     */
    public static String convertMessage(Player player, String message, String format) {
        //local vars
        String playerName = "";
        //check for global
        if (player == null) {
            playerName = "Global";
        }
        else {
            playerName = player.getName();
        }
        //swap out placeholders
        format = format.replaceAll("%player%", playerName);
        format = format.replaceAll("%message%", message);
        //color support
        format = ChatColor.translateAlternateColorCodes('&', format);
        return format;
    }
    /**
     *
     * This method sends a given message to a player
     *
     * @param player The player to send to
     * @param message the message to send
     */
    public static void sendMessage(Player player, String message) {
        //color support
        message = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(message);
    }
    /**
     *
     * This method sends a message to all online players
     * If you want to send to only inGame players, set inGameOnly to true
     * If you want to send to only a specific arena's players, set certainArena to true and specify an arena
     *
     * @param message the message to send
     * @param inGameOnly true for inGAme users only
     * @param certainArena true to send in the same arena
     * @param arena the arena object to use
     * @param plugin the plugin to send messages to
     */
    public static void sendMessageToAll(String message, boolean inGameOnly, boolean certainArena, Arena arena, Plugin plugin) {
        //Ingot Player
        IngotPlayer currentIPlayer = null;
        //color support
        message = ChatColor.translateAlternateColorCodes('&', message);
        //check if should only send to players ingame
        if (inGameOnly == true) {
            //cycle between all online players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //get current ingotplayer
                currentIPlayer = IngotPlayer.selectPlayer(key.getName(), plugin);
                //check if player is ingame
                if (currentIPlayer.getInGame() == true && certainArena == false) {
                    //send message
                    key.sendMessage(message);
                }
                //check if player is in the selected arena
                if (currentIPlayer.getGame().equalsIgnoreCase(arena.getName()) && certainArena == true) {
                    //send message
                    key.sendMessage(message);
                }
            }
        }
        //send to all players
        else {
            //send message
            Bukkit.broadcastMessage(message);
        }
    }
}
