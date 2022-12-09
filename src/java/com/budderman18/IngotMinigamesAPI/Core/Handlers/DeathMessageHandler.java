package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;


/**
 *
 * This class handles death messages
 * You'll still need an event to run to activate this
 * 
 */
public class DeathMessageHandler {
    //global vars
    private HashMap<DeathMessageType,String> messages = new HashMap<>();
    private Plugin plugin = null;
    private static ArrayList<DeathMessageHandler> messagelist = new ArrayList<>();
    /**
     * 
     * This constructor blocks new9) usage
     * 
     */
    private DeathMessageHandler(Plugin pluginn) {
        this.messages = new HashMap<>();
        this.plugin = pluginn;
    }
    /**
     * 
     * This method formats a message using %player% %killer% and %item% placeholders
     * 
     * @param messageToFormat the message to format
     * @param victum the playername who died
     * @param killer the playername who died
     * @param item the item used to kill
     * @return the formatted message
     */
    public static String formatMessage(String messageToFormat, String victum, String killer, String item) {
        //local vars
        String message = ChatColor.translateAlternateColorCodes('&', messageToFormat);
        //check for player
        if (message.contains("%player%")) {
            message = message.replace("%player%", victum);
        }
        //check for killer
        if (message.contains("%killer%")) {
            message = message.replace("%killer%", killer);
        }
        //check for item
        if (message.contains("%item%")) {
            message = message.replace("%item%", item);
        }
        //return message
        return message;
    }
    /**
     * 
     * This method updates the stored message list
     * 
     * @param pluginn plugin to search for
     */
    private static HashMap<DeathMessageType,String> obtainMessages(Plugin pluginn) {
        //cycle though all plugins
        for (DeathMessageHandler key : messagelist) {
            //chceck if plugin is equal to desired
            if (key.plugin == pluginn) {
                //set messages and return
                return key.messages;
            }
        }
        //add new messagelist
        messagelist.add(new DeathMessageHandler(pluginn));
        return messagelist.get(messagelist.size()-1).messages;
    }
    /**
     * 
     * This method replaces a cause on the message list
     * It is capable of creating new messages for causes if it lacks one
     * 
     * @param message the unformatted message
     * @param cause the cause using DeathMessageType
     * @param pluginn The plugin to attach to
     */
    public static void replaceMessage(String message, DeathMessageType cause, Plugin pluginn) {
        //local vars
        HashMap<DeathMessageType,String> messagess = obtainMessages(pluginn);
        //check if cause doesnt exist
        if (!(messagess.keySet().contains(cause))) {
            //add cause
            messagess.put(cause, message);
        }
        //run if already exists
        else {
            messagess.replace(cause, message);
        }
    }
    /**
     * 
     * This method gets a message using its cause
     * 
     * @param cause the cause to search for
     * @param pluginn the plugin to search for
     * @return the message found
     */
    public static String getMessage(DeathMessageType cause, Plugin pluginn) {
        //return string
        return obtainMessages(pluginn).get(cause);
    }
    /**
     * 
     * This method gets the hashmap containing all message
     * 
     * @param pluginn the plugin to search for
     * @return the messages map
     */
    public static HashMap<DeathMessageType,String> getMessages(Plugin pluginn) {
        //return the message map
        return obtainMessages(pluginn);
    }
}
