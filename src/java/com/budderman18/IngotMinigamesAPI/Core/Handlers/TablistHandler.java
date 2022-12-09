package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Main;
import org.bukkit.Bukkit;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.WorldNBTStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles everything involving the tablist 
 * This includes headers & footers, as well as what players are in it
 *
 */
public class TablistHandler {
    //global vars
    private static final Plugin plugin = Main.getInstance();
    /**
     * 
     * This constructor blocks new() usage as it does nothing
     * 
     */
    private TablistHandler() {}
    /**
     *
     * This method sets the header for the tablist
     *
     * @param player the player to affecet
     * @param header the text to use
     */
    public static void setHeader(Player player, String header) {
        //set header
        player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', header));
    }

    /**
     *
     * This method sets the footer for the tablist
     *
     * @param player the player to affect
     * @param footer the text to use
     */
    public static void setFooter(Player player, String footer) {
        //set footer
        player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', footer));
    }

    /**
     *
     * This method removes players from the tablist that are not in the specified arena. 
     * Useful for showing exactly what players are in-game on a large server.
     * This uses NMS and will likely not work on other versions.
     * DOES NOT WORK AT THE MOMENT, DO NOT USE!!!
     *
     * @param arena The arena to use
     */
    @Deprecated
    public static void removePlayers(Arena arena) {
        //needed, no idea why
        MinecraftServer server = MinecraftServer.getServer();
        Dimension dimension = Dimension.BUILTIN.get();
        WorldNBTStorage storage = server.playerDataStorage;
        IngotPlayer iplayer = null;
        int integer = 0;
        //global vars
        PlayerList list = new PlayerList(server, dimension, storage, integer) {};
        EntityPlayer player = null;
        //cycle through players
        for (Player key : Bukkit.getOnlinePlayers()) {
            //get player
            iplayer = IngotPlayer.selectPlayer(key.getName(), plugin);
            //check if player is not in game and playing in the arena
            if (!(iplayer.getInGame() == true && iplayer.getGame().equalsIgnoreCase(arena.getName()))) {
                //remove from list
                player = (EntityPlayer) key;
                list.remove(player);
            }
        }
    }
    /**
     *
     * This method resets the tablist Used when leaving
     *
     * @param player The player to affect
     */
    public static void reset(Player player) {
        //reset header/footer
        player.setPlayerListHeaderFooter("", "");
        //cycle all online players
        for (Player key : Bukkit.getOnlinePlayers()) {
            //reset playername
            key.setPlayerListName(key.getName());
        }
    }
}
