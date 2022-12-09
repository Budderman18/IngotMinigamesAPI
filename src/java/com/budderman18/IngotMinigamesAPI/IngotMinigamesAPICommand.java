package com.budderman18.IngotMinigamesAPI;

import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * 
 * This class handles all the commands normal players use. 
 * 
 */
public class IngotMinigamesAPICommand implements TabExecutor {
    //retrive plugin instance
    private static final Plugin plugin = Main.getInstance();
    //files
    private static FileConfiguration config = FileManager.getCustomData(plugin, "config", "");
    private static FileConfiguration language = FileManager.getCustomData(plugin, "language", "");
    //messages
    private static String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message") + "");
    private static String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message") + "");
    private static String configEditedMessage1 = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Config-Edited-Message-1") + "");
    private static String configEditedMessage2 = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Config-Edited-Message-2") + "");
    private static String invalidArgumentMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Invalid-Argument-Message") + "");
    private static String helpStartMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Start-Message") + "");
    private static String helpConfigMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Config-Message") + "");
    private static String helpHelpMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Help-Message") + "");
    private static String helpReloadMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Reload-Message") + "");
    private static String helpVersionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Version-Message") + "");
    private static String helpEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-End-Message") + "");
    private static String reloadMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Reload-Message") + "");
    private static String versionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Version-Message") + "");
    /**
     * 
     * This method reloads files + messages for IMAPI
     * 
     */
    public static void reload() {
        //files
        config = FileManager.getCustomData(plugin, "config", "");
        language = FileManager.getCustomData(plugin, "language", "");
        //messages
        prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message") + "");
        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message") + "");
        invalidArgumentMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Invalid-Argument-Message") + "");
        configEditedMessage1 = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Config-Edited-Message-1") + "");
        configEditedMessage2 = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Config-Edited-Message-2") + "");
        helpStartMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Start-Message") + "");
        helpConfigMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Config-Message") + "");
        helpHelpMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Help-Message") + "");
        helpReloadMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Reload-Message") + "");
        helpVersionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-Version-Message") + "");
        helpEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Help-End-Message") + "");
        reloadMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Reload-Message") + "");
        versionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("IMAPI-Version-Message") + "");
    }
    /**
     *
     * This method handles the SG command. 
     *
     * @param sender the command sender
     * @param cmd the command object
     * @param label the command name
     * @param args the command args
     * @return true if valid
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //local vars
        List<String> editableOptions = new ArrayList<>();
	//check if command is valid
        if (cmd.getName().equalsIgnoreCase("IMAPI")) {
            //check for no arguments
            if (args.length == 0) {
                return false;
            }
            //check for config
            if (args[0].equalsIgnoreCase("config")) {
                //check for permission
                if (sender.hasPermission("IngotMinigamesAPI.IMAPI.config")) {
                    //cycle through file
                    for (String key : config.getKeys(false)) {
                        //check if not on version
                        if (!key.equalsIgnoreCase("version")) {
                            //add option
                            editableOptions.add(key);
                        }
                    }
                    //check if option is valid
                    if (editableOptions.contains(args[1]) && args.length > 2) {
                        //set file
                        config.set(args[1], args[2]);
                        //save file
                        try {
                            config.save(new File(plugin.getDataFolder(), "config.yml"));
                        }
                        catch (IOException ex) {
                            //check for debug mode
                            if (config.getBoolean("enable-debug-mode") == true) {
                                //log error
                                Logger.getLogger(IngotMinigamesAPICommand.class.getName()).log(Level.SEVERE, "COULD NOT SAVE CONFIG.YML!");
                            }
                        }
                        sender.sendMessage(prefixMessage + configEditedMessage1 + args[1] + configEditedMessage2 + args[2]);
                        return true;
                    }
                    //run if invalid
                    else {
                        sender.sendMessage(prefixMessage + invalidArgumentMessage);
                        return true;
                    }
                }
                //run if lacking permission
                else {
                    sender.sendMessage(prefixMessage + noPermissionMessage);
                    return true;
                }
            }
            //check for help
            if (args[0].equalsIgnoreCase("help")) {
                //check for permission
                if (sender.hasPermission("IngotMinigamesAPI.IMAPI.help")) {
                    //send menu
                    sender.sendMessage(prefixMessage + helpStartMessage);
                    sender.sendMessage(prefixMessage + helpConfigMessage);
                    sender.sendMessage(prefixMessage + helpHelpMessage);
                    sender.sendMessage(prefixMessage + helpReloadMessage);
                    sender.sendMessage(prefixMessage + helpVersionMessage);
                    sender.sendMessage(prefixMessage + helpEndMessage);
                    return true;
                }
            }
            //check for reload
            if (args[0].equalsIgnoreCase("reload")) {
                //check for permission
                if (sender.hasPermission("IngotMinigamesAPI.IMAPI.reload")) {
                    //reload
                    Main.createFiles();
                    reload();
                    sender.sendMessage(prefixMessage + reloadMessage);
                    return true;
                }
            }
            //check for version
            if (args[0].equalsIgnoreCase("version")) {
                //check for permission
                if (sender.hasPermission("IngotMinigamesAPI.IMAPI.version")) {
                    //send version
                    sender.sendMessage(prefixMessage + versionMessage + config.getString("version"));
                    return true;
                }
            }
            //run if bad arguments
            sender.sendMessage(prefixMessage + invalidArgumentMessage);
            return true;
	}
	//should never have to run, but won't compile without it
        return false;
    }
    /**
     *
     * This method handles tabcompletion when required. 
     *
     * @param sender the sender object
     * @param command the command object
     * @param alias the other commands that run the same
     * @param args the command args
     * @return the tabcompletion list
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //local vars
        List<String> arguments = new ArrayList<>();
        //default args
        if (args.length == 1) {
            arguments.add("config");
            arguments.add("help");
            arguments.add("reload");
            arguments.add("version");
        }
        //config args
        if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
            //cycle through file
            for (String key : config.getKeys(false)) {
                //check if not on version
                if (!key.equalsIgnoreCase("version")) {
                    //add option
                    arguments.add(key);
                }
            }
        }
        //debug-mode vars
        if (args.length == 3 && args[1].equalsIgnoreCase("enable-debug-mode")) {
            arguments.add("false");
            arguments.add("true");
        }
        return arguments;
    }
}