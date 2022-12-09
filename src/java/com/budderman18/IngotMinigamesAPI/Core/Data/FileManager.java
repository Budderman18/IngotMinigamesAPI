package com.budderman18.IngotMinigamesAPI.Core.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * 
 * This class manages anything file related.
 * It can also auto-update files with new variables 
 * when the methods are configured.
 * 
 */
public class FileManager {
    //local vars
    private static final String ROOT = "";
    /**
     *
     * This method is used to read and write to a given file. Also handles YML
     * loading if its a yml file
     *
     * @param plugin The plugin to use for directory path
     * @param filename the name of the file
     * @param path the filepath beyond the plugin, null if same directory
     * @return FileConfiguration The file config that was locatied
     */
    public static YamlConfiguration getCustomData(Plugin plugin, String filename, String path) {
        //create file object
        File file = null;
        File pluginFolder = null;
        //check if using root directory rather than a plugin
        if (plugin != null) {
            pluginFolder = plugin.getDataFolder();
        }
        else {
            pluginFolder = Bukkit.getWorldContainer();
        }
        //check if folder is a thing
        if (!pluginFolder.exists()) {
            //create plugin folder
            pluginFolder.mkdir();
        }
        //check if file is a yml
        if (!filename.contains(".")) {
            //obtain file
            file = new File(pluginFolder + "/" + path, filename + ".yml");
        }
        //if file isn't yml, run this
        else {
            //obtain file
            file = new File(pluginFolder + "/" + path, filename);
        }
        //load config
        return YamlConfiguration.loadConfiguration(file);
    }
    /**
     *
     * This method obtains all the loaded arena names
     * You should use Arena.getArenas() instead, as you
     * should always have all arenas loaded (that one is faster)
     *
     * @param plugin The plugin to search for
     * @return The arenas located
     */
    @Deprecated
    public static List<String> getArenas(Plugin plugin) {
        //local vars
        File filePath = new File(plugin.getDataFolder() + "/Arenas/");
        FileConfiguration temp = null;
        String[] listingAllFiles = filePath.list();
        List<String> output = new ArrayList<>();
        //cycle through all files
        for (String file : listingAllFiles) {
            //load settings
            temp = FileManager.getCustomData(plugin, "settings", "/Arenas/" + file + '/');
            //add to list
            output.add(temp.getString("name"));
        }
        //return list
        return output;
    }
}