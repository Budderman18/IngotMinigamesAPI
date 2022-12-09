package com.budderman18.IngotMinigamesAPI;

import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class enables and disables the plugin
 * It also imports commands and handles events
 */
public class Main extends JavaPlugin { 
    //files
    private static Main plugin = null;
    private final ConsoleCommandSender sender = getServer().getConsoleSender();
     /**
     *
     * This method retrieves the current plugin data
     *
     * @return the plugin
     */
    public static Main getInstance() {
        return plugin;
    }
    /**
     *
     * This method creates files if needed. 
     * Only needed if file is missing (first usage). 
     *
     */
    public static void createFiles() {
        //config file
        File configf = new File(plugin.getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
         }
        //config object
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } 
        catch (IOException | InvalidConfigurationException e) {}
        //langauge file
        File languagef = new File(plugin.getDataFolder(), "language.yml");
        if (!languagef.exists()) {
            languagef.getParentFile().mkdirs();
            plugin.saveResource("language.yml", false);
         }
        //langauge object
        FileConfiguration language = new YamlConfiguration();
        try {
            language.load(languagef);
        } 
        catch (IOException | InvalidConfigurationException e) {}
    } 
    /**
     *
     * Enables the plugin.
     * Checks if MC version isn't the latest.
     * If its not, warn the player about lacking support
     * Checks if server is running offline mode
     * If it is, disable the plugin
     * Also loads death event
     *
     */
    @Override
    public void onEnable() {
        //get plugin
        plugin = this;
        //create files
        createFiles();
        //language variables
        FileConfiguration language = FileManager.getCustomData(plugin, "language", "");
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message") + "");
        String unsupportedVersionAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-ServerA-Message") + ""); 
        String unsupportedVersionBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-ServerB-Message") + "");
        String unsupportedVersionCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-ServerC-Message") + "");
        String unsecureServerAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerA-Message") + "");
        String unsecureServerBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerB-Message") + "");
        String unsecureServerCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerC-Message") + "");
        String pluginEnabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Enabled-Message") + "");
        //check for correct version
        if (!(Bukkit.getVersion().contains("1.19.3"))) {
            sender.sendMessage(prefixMessage + unsupportedVersionAMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionBMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionCMessage);  
        }
        //check for online mode
        if (getServer().getOnlineMode() == false && FileManager.getCustomData(null, "spigot", "").getBoolean("settings.bungeecord") == false) {
            sender.sendMessage(prefixMessage + unsecureServerAMessage);
            sender.sendMessage(prefixMessage + unsecureServerBMessage);
            sender.sendMessage(prefixMessage + unsecureServerCMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
        //enable command
        this.getCommand("IMAPI").setExecutor(new IngotMinigamesAPICommand());
        //enable plugin
        getServer().getPluginManager().enablePlugin(this);
        sender.sendMessage(prefixMessage + pluginEnabledMessage);
    }
    /**
     *
     * This method disables the plugin
     *
     */
    @Override
    public void onDisable() {
        //local vars
        FileConfiguration language = FileManager.getCustomData(plugin, "language", "");
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message") + "");
        String pluginDisabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Disabled-Message") + "");
        //disables plugin
        getServer().getPluginManager().disablePlugin(this);
        sender.sendMessage(prefixMessage + pluginDisabledMessage);
    }
}
