package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles all necessary playerdata for IngotMinigamesAPI
 * You can't extend this class due to enums being unextendable
 * LeaderboardType is used to properly setup a certain leaderboard.
 * If you want to make a new leaderboard, suggest it to us or use your own system.
 * 
 */
public class Leaderboard {
    //files
    private static Plugin staticPlugin = Main.getInstance();
    private static final String ROOT = "";
    private static FileConfiguration config = FileManager.getCustomData(staticPlugin, "config", ROOT);
    //leaderboard vars vars
    private String name = "";
    private LeaderboardType type = LeaderboardType.SCORE;
    private List<IngotPlayer> players = new ArrayList<>();
    private List<ArmorStand> hologram = new ArrayList<>();
    private Location holoLoc = null;
    private byte maxSize = 0;
    private boolean invertList = false;
    private boolean summoned = false;
    private Plugin plugin = null;
    private int index = 0;
    //global vars
    private static int trueIndex = 0;
    private static List<Leaderboard> boards = new ArrayList<>();
    /**
     * 
     * This constructor blocks new() methods.
     * This forces people to use create() instead
     * 
     */
    private Leaderboard() {}
    /**
     * 
     * This method creates a new HubPlayer with the given data
     * Set unused vars to null
     * 
     * @param namee the name to make
     * @param typee the type of leaderboard
     * @param playerss the players in the leaderboard
     * @param hologramm the hologram lines
     * @param holoLocc the location for the leaderboard holograms
     * @param maxSizee the maxSize for the hologram (NOT the playerlist)
     * @param invertListt show hologram backwards (lowest first)
     * @param summonedd weather or not hologram is summoned
     * @param pluginn the plugin to attach to
     * @return the created leaderboard
     */
    public static Leaderboard createBoard(String namee, LeaderboardType typee, List<IngotPlayer> playerss, List<ArmorStand> hologramm, Location holoLocc, byte maxSizee, boolean invertListt, boolean summonedd, Plugin pluginn) {
        //create object
        Leaderboard board = new Leaderboard();
        //setup vars
        board.name = namee;
        board.type = typee;
        if (playerss == null) {
            playerss = new ArrayList<>();
        }
        board.players = playerss;
        if (hologramm == null) {
            hologramm = new ArrayList<>();
        }
        board.hologram = hologramm;
        board.holoLoc = holoLocc;
        board.maxSize = maxSizee;
        board.invertList = invertListt;
        board.summoned = summonedd;
        board.plugin = pluginn;
        board.index = trueIndex;
        //add to list and return
        boards.add(board);
        trueIndex++;
        return board;
    }
    /**
     * 
     * This method deletes the selected leaderbord
     * 
     */
    public void deleteBoard() {
        //decrement all higher indexes to prevent bugs
        for (Leaderboard key : boards) {
            if (key.index > this.index) {
                boards.get(key.index).index--;
            }
        }
        boards.remove(this);
        trueIndex--;
        this.name = null;
        this.type = null;
        this.players = null;
        this.hologram = null;
        this.holoLoc = null;
        this.plugin = null;
        this.index = 0;
    }
    /**
     * 
     * This method selects a specified player using their name.
     * 
     * @param namee the name to search
     * @param pluginn the plugin to use
     * @return the returned board
     */
    public static Leaderboard selectBoard(String namee, Plugin pluginn) {
        //cycle through all boards
        for (Leaderboard key : boards) {
            //check if name is null
            if (key.getName() != null) {
                //check if desired board
                if (key.getName().equalsIgnoreCase(namee) && key.getPlugin() == pluginn) {
                    return key;
                }
            }
        }
        //check for debug mode
        if (config.getBoolean("enable-debug-mode") == true) {
            //log error
            Logger.getLogger(Leaderboard.class.getName()).log(Level.SEVERE, "COULD NOT SELECT LEADERBOARD " + namee + '!');
        }
        return null;
    }
    /**
     * 
     * This method gets all loaded instances
     * Instances with null names are ignored (they shouldn't exist)
     * 
     * @return the instance list
     */
    public static List<Leaderboard> getInstances() {
        //local vars
        List<Leaderboard> playerss = new ArrayList<>();
        //cycle through leaderbords
        for (Leaderboard key : boards) {
            //check if name is not null
            if (key.name != null) {
                //add
                playerss.add(key);
            }
            //delete if null
            else {
                key.deleteBoard();
            }
        }
        return playerss;
    }
    /**
     * 
     * This method saves the leaderboard to the file
     * this requires the location not being null (will cancel if it is)
     * 
     * @param savePlayers true to save playerNames to the file
     */
    public void saveToFile(boolean savePlayers) {
        //local vars
        File hologramf = new File(this.plugin.getDataFolder(), "hologram.yml");
        FileConfiguration hologram = FileManager.getCustomData(this.plugin, "hologram", "");
        List<String> playerNames = new ArrayList<>();
        //check if location is null
        if (this.holoLoc == null) {
            //dont save
            return;
        }
        //set file
        hologram.set(this.name + ".type", this.type.toString());
        //check for savePlayers
        if (savePlayers == true) {
            //cycle through all players
            for (IngotPlayer key : this.players) {
                //add player
                playerNames.add(key.getUsername());
            }
            //set list
            hologram.set(this.name + ".players", playerNames);
        }
        hologram.set(this.name + ".location.world", this.holoLoc.getWorld().getName());
        hologram.set(this.name + ".location.x", this.holoLoc.getX());
        hologram.set(this.name + ".location.y", this.holoLoc.getY());
        hologram.set(this.name + ".location.z", this.holoLoc.getZ());
        hologram.set(this.name + ".location.world", this.holoLoc.getWorld().getName());
        hologram.set(this.name + ".max-size", this.maxSize);
        hologram.set(this.name + ".invert-list", this.invertList);
        hologram.set(this.name + ".summoned", this.summoned);
        //save file
        try {
            hologram.save(hologramf);
        } 
        catch (IOException ex) {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(IngotPlayer.class.getName()).log(Level.SEVERE, "COULD NOT SAVE HOLOGRAM.YML!");
            }
        }
    }
    /**
     * 
     * This method organizes the leaderboard.
     * You'll need this to be ran in order to properly update the hologram
     * 
     * @param saveList weather or not to overwrite the current list
     * @return the organized list
     */
    public List<IngotPlayer> organizeLeaderboard(boolean saveList) {
        //local vars
        List<IngotPlayer> playerss = new ArrayList<>();
        short divideby = 1;
        short dividebyy = 1;
        //check if valid players
        if (this.players != null) {
            //use current list
            playerss = this.players;
        }
        //cycle through the player list
        for (int i=1; i < playerss.size(); i++) {
            //check if below starting point
            if (i < 1) {
                i=1;
            }
            //check for kills
            if (this.type == LeaderboardType.KILLS) {
                //check for invertlist
                if (this.invertList == false) {
                    //check if currentPlayer has more kills than the previous
                    if (playerss.get(i-1).getKills() < playerss.get(i).getKills()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPLayer has less kills than the previous
                    if (playerss.get(i-1).getKills() > playerss.get(i).getKills()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
            //check for deaths
            else if (this.type == LeaderboardType.DEATHS) {
                //check for invert list
                if (this.invertList == false) {
                    //check if currentPlayer has more deaths than the previous
                    if (playerss.get(i-1).getDeaths() < playerss.get(i).getDeaths()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPLayer has less deaths than the previous
                    if (playerss.get(i-1).getDeaths() > playerss.get(i).getDeaths()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
            //check for wins
            else if (this.type == LeaderboardType.WINS) {
                //check for invertList
                if (this.invertList == false) {
                    //check if currentPlayer has more wins than previous
                    if (playerss.get(i-1).getWins() < playerss.get(i).getWins()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPlayer has less wins than previous
                    if (playerss.get(i-1).getWins() > playerss.get(i).getWins()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
            //check for losses
            else if (this.type == LeaderboardType.LOSSES) {
                //check for invertList
                if (this.invertList == false) {
                    //check if currentPlayer has more losses than previous
                    if (playerss.get(i-1).getLosses() < playerss.get(i).getLosses()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPlayer has less losses than previous
                    if (playerss.get(i-1).getLosses() > playerss.get(i).getLosses()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
            //check for kdratio
            else if (this.type == LeaderboardType.KDRATIO) {
                //check if previous player's deaths is more than 0
                if (playerss.get(i-1).getDeaths() > 0) {
                    //set divideby
                    divideby = playerss.get(i-1).getDeaths();
                }
                //check if current player's deaths is more than 0
                if (playerss.get(i).getDeaths() > 0) {
                    //set dividebyy
                    dividebyy = playerss.get(i).getDeaths();
                }
                //check for invertlist
                if (this.invertList == false) {
                    //check if currentPlayer has a greater k/d than the previous
                    if (((float) (playerss.get(i-1).getKills()) / (float) (divideby)) < ((float) (playerss.get(i).getKills()) / (float) (dividebyy))) {
                        //move player backward
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPLayer has a lesser k/d than the previous
                    if (((float) (playerss.get(i-1).getKills()) / (float) (divideby)) < ((float) (playerss.get(i).getKills()) / (float) (dividebyy))) {
                        //move player backward
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
            //check for wlratio
            else if (this.type == LeaderboardType.WLRATIO) {
                //check if previous player's losses is greater than 0
                if (playerss.get(i-1).getLosses() > 0) {
                    //set divideby
                    divideby = playerss.get(i-1).getLosses();
                }
                //check if current player's losses is grater than 0
                if (playerss.get(i).getLosses() > 0) {
                    //set dividebyy
                    dividebyy = playerss.get(i).getLosses();
                }
                //check for invertList
                if (this.invertList == false) {
                    //check if currentPlayer's w/l is greater than the previous
                    if (((float) (playerss.get(i-1).getWins()) / (float) (divideby)) < ((float) (playerss.get(i).getWins()) / (float) (dividebyy))) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPlayer's w/l is less than the previous
                    if (((float) (playerss.get(i-1).getWins()) / (float) (divideby)) < ((float) (playerss.get(i).getWins()) / (float) (dividebyy))) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
            //check for score
            else if (this.type == LeaderboardType.SCORE) {
                //check for invertList
                if (this.invertList == false) {
                    //check if currentPlayer has more score than previous
                    if (playerss.get(i-1).getScore() < playerss.get(i).getScore()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
                else {
                    //check if currentPlayer has less score than previous
                    if (playerss.get(i-1).getScore() > playerss.get(i).getScore()) {
                        //move player backwards
                        playerss.add(i-1,playerss.get(i));
                        playerss.remove(i+1);
                        //decrement counter by 2 to allow for multiple checks
                        i-=2;
                    }
                }
            }
        }
        //check for saveList
        if (saveList == true) {
            this.setPlayers(playerss);
        }
        //return generated list
        return playerss;
    }
    /**
     * 
     * This method summons a new hologram with the specified header and footer
     * use %player% for playername and %value% for the value
     * 
     * @param header the very first line
     * @param footer the very last line
     * @param format the format to use
     * @param saveList weather or not to overwrite the hologram list
     * @return the hologram list
     */
    public List<ArmorStand> summonHologram(String header, String format, String footer, boolean saveList) {
        //local vars
        List<ArmorStand> hologramm = new ArrayList<>();
        ArmorStand line = null;
        Location holoLocc = this.holoLoc;
        String newFormat = format;
        byte indexx = 0;
        //check for holo
        if (this.hologram != null && !(this.hologram.isEmpty())) {
            hologramm = this.hologram;
            this.killHologram(true);
        }
        //generate new armorstand
        line = (ArmorStand) holoLocc.getWorld().spawnEntity(holoLocc, EntityType.ARMOR_STAND);
        line.setInvisible(true);
        line.setInvulnerable(true);
        line.setCustomNameVisible(true);
        line.setGravity(false);
        //check for type
        if (header.contains("%type%")) {
            //set new header
            header = header.replace("%type%", this.name);
        }
        //set armorstand's name
        line.setCustomName(ChatColor.translateAlternateColorCodes('&', header));
        //check if list can be overwritten
        try {
            hologramm.set(indexx, line);
        }
        //add instead if can't be
        catch (IndexOutOfBoundsException ex) {
            hologramm.add(line);
        }
        //cycle through players
        for (IngotPlayer key : this.players) {
            //check if reached the max size
            if (hologramm.size() > this.maxSize) {
                break;
            }
            //update y value
            indexx++;
            holoLocc.setY(holoLocc.getY()-0.25);
            //check for player
            if (format.contains("%player%")) {
                //set new format
                newFormat = newFormat.replace("%player%", key.getUsername() + " ");
            }
            //check for value
            if (format.contains("%value%")) {
                //check for kills
                if (this.type == LeaderboardType.KILLS) {
                    //set new format
                    newFormat = newFormat.replace("%value%", Short.toString(key.getKills()));
                }
                //check for deaths
                else if (this.type == LeaderboardType.DEATHS) {
                    //set new format
                    newFormat = newFormat.replace("%value%", Short.toString(key.getDeaths()));
                }
                //check for wins
                else if (this.type == LeaderboardType.WINS) {
                    //set new format
                    newFormat = newFormat.replace("%value%", Short.toString(key.getWins()));
                }
                //check losses
                else if (this.type == LeaderboardType.LOSSES) {
                    //set new format
                    newFormat = newFormat.replace("%value%", Short.toString(key.getLosses()));
                }
                //check kd
                else if (this.type == LeaderboardType.KDRATIO) {
                    //check if deaths does not equal 0
                    if (key.getDeaths() != 0) {
                        //set new format
                        newFormat = newFormat.replace("%value%", Float.toString((float) ((key.getKills() + 0.0) / (key.getDeaths() + 0.0))));
                    }
                    else {
                        //set new format
                        newFormat = newFormat.replace("%value%", Float.toString(key.getKills()));
                    }
                }
                //check wl
                else if (this.type == LeaderboardType.WLRATIO) {
                    //check if losses does not equal 0
                    if (key.getLosses() != 0) {
                        //set new format
                        newFormat = newFormat.replace("%value%", Float.toString((float) ((key.getWins() + 0.0) / (key.getLosses() + 0.0))));
                    }
                    else {
                        //set new format
                        newFormat = newFormat.replace("%value%", Float.toString(key.getWins()));
                    }
                }
                //check score
                else if (this.type == LeaderboardType.SCORE) {
                    //set new format
                    newFormat = newFormat.replace("%value%", Short.toString(key.getScore()));
                }
            }
            //generate new armorstand
            line = (ArmorStand) holoLocc.getWorld().spawnEntity(holoLocc, EntityType.ARMOR_STAND);
            line.setInvisible(true);
            line.setInvulnerable(true);
            line.setCustomNameVisible(true);
            line.setGravity(false);
            //set armorstand name
            line.setCustomName(ChatColor.translateAlternateColorCodes('&', indexx + ". " + newFormat));
            //check if list can be overwritted
            try {
                hologramm.set(indexx, line);
            }
            //run if it can't be
            catch (IndexOutOfBoundsException ex) {
                hologramm.add(line);
            }
            //reset line and format
            line = null;
            newFormat = format;
        }
        //set y value
        holoLocc.setY(holoLocc.getY()-0.25);
        //set footer
        //generate new armorstand
        line = (ArmorStand) holoLocc.getWorld().spawnEntity(holoLocc, EntityType.ARMOR_STAND);
        line.setInvisible(true);
        line.setInvulnerable(true);
        line.setCustomNameVisible(true);
        line.setGravity(false);
        //check for type
        if (footer.contains("%type%")) {
            //set new footer
            footer = footer.replace("%type%", this.name);
        }
        //set armorstand name
        line.setCustomName(ChatColor.translateAlternateColorCodes('&', footer));
        //check if hologram can be overwritten
        try {
            hologramm.set(indexx+1, line);
        }
        //run if can't be
        catch (IndexOutOfBoundsException ex) {
            hologramm.add(line);
        }
        //set summoned
        this.setSummoned(true);
        //check for saveList
        if (saveList == true) {
            this.setHologram(hologramm);
        }
        this.holoLoc.setY(this.holoLoc.getY() + ((double)(this.hologram.size() - 1))/(double)4);
        //return generated hologram
        return hologramm;
    }
    /**
     * 
     * This method kills the selected hologram
     * 
     * @param setSummoned weather or not to set summoned status
     */
    public void killHologram(boolean setSummoned) {
        //local vars
        byte antiinf = 0;
        while (!this.hologram.isEmpty() && antiinf != 127) {
            //cycle through hologram
            try {
                for (ArmorStand key : this.hologram) {
                    //delete armorstand
                    key.remove();
                    this.hologram.remove(key);
                }
                if (setSummoned == true) {
                //set not summoned
                    this.setSummoned(false);
                }
            }
            catch (ConcurrentModificationException ex) {
                if (config.getBoolean("enable-debug-mode") == true) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "COULD NOT KILL HOLOGRAM FOR LEADERBOARD " + this.getName() + '!');
                }
            }
            antiinf++;
        }
    }
    /**
     *
     * This method adds a player to the selected leaderboard. 
     *
     * @param iplayer the ingotplayer to add
     */
    public void addPlayer(IngotPlayer iplayer) {
        //add instance list
        if (boards.contains(this)) {
            boards.get(this.index).players.add(iplayer);
        }
        //add selection
        this.players.add(iplayer);
    }
    /**
     *
     * This method removes a player to the selected leaderboard. 
     *
     * @param iplayer the ingotplayer to remove
     */
    public void removePlayer(IngotPlayer iplayer) {
        //add instance list
        if (boards.contains(this)) {
            boards.get(this.index).players.remove(iplayer);
        }
        //add selection
        this.players.remove(iplayer);
    }
    /**
     *
     * This method adds a line to the hologram of the selected leaderboard. 
     *
     * @param linee the line to add
     */
    public void addLine(ArmorStand linee) {
        //add instance list
        if (boards.contains(this)) {
            boards.get(this.index).hologram.add(linee);
        }
        //add selection
        this.hologram.add(linee);
    }
    /**
     *
     * This method removes  a line to the hologram of the selected leaderboard. 
     *
     * @param linee the line to remove
     */
    public void removeLine(ArmorStand linee) {
        //add instance list
        if (boards.contains(this)) {
            boards.get(this.index).hologram.remove(linee);
        }
        //add selection
        this.hologram.remove(linee);
    }
    /**
     * 
     * This method sets the name of the selected leaderboard
     * If the name is null, it won't apply (null names break selections)
     * 
     * @param namee the name to set
     */
    public void setName(String namee) {
        if (namee != null) {
            //set username in list
            if (boards.contains(this)) {
                boards.get(this.index).name = namee;
            }
            //set selected username
            this.name = namee;
        }
        else {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(Leaderboard.class.getName()).log(Level.SEVERE, "COULD NOT SET THE NAME OF PLAYER " + namee + " BECAUSE THE NAME WOULD BECOME NULL!");
            }
        }
    }
    /**
     *
     * This method gets the name for the selected hubplayer.
     *
     * @return the name
     */
    public String getName() {
        //return instance username
        if (boards.contains(this)) {
            return boards.get(this.index).name;
        }
        //return selected username as a fallback
        return this.name;
    } 
    /**
     * 
     * This method sets the type of the selected leaderboard
     * 
     * @param typee the type to set
     */
    public void setType(LeaderboardType typee) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).type = typee;
        }
       //set selection
        this.type = typee;
    }
    /**
     *
     * This method gets the type for the selected leaderboard.
     *
     * @return the type
     */
    public LeaderboardType getType() {
       //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).type;
        }
        //return selection as a fallback
        return this.type;
    }
    /**
     * 
     * This method sets the players of the selected leaderboard
     * 
     * @param playerss the players to set
     */
    public void setPlayers(List<IngotPlayer> playerss) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).players = playerss;
        }
       //set selection
        this.players = playerss;
    }
    /**
     *
     * This method gets the players for the selected leaderboard.
     *
     * @return the players
     */
    public List<IngotPlayer> getPlayers() {
       //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).players;
        }
        //return selection as a fallback
        return this.players;
    }
    /**
     * 
     * This method sets the players of the selected leaderboard
     * 
     * @param hologramm the hologram to set
     */
    public void setHologram(List<ArmorStand> hologramm) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).hologram = hologramm;
        }
       //set selection
        this.hologram = hologramm;
    }
    /**
     *
     * This method gets the hologram for the selected leaderboard.
     *
     * @return the hologram
     */
    public List<ArmorStand> getHologram() {
       //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).hologram;
        }
        //return selection as a fallback
        return this.hologram;
    }
    /**
     * 
     * This method sets the hologram Location of the selected leaderboard
     * 
     * @param holoLocc the hologram's location
     */
    public void setHoloLoc(Location holoLocc) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).holoLoc = holoLocc;
        }
        //set selection
        this.holoLoc = holoLocc;
    }
    /**
     *
     * This method gets the hologram lcoation for the selected leaderboard.
     *
     * @return the hologramLocation
     */
    public Location getHoloLoc() {
       //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).holoLoc;
        }
        //return selection as a fallback
        return this.holoLoc;
    }
    /**
     * 
     * This method sets the maxSize of the selected leaderboard
     * 
     * @param size the max amount to allow
     */
    public void setMaxSize(byte size) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).maxSize = size;
        }
        //set selection
        this.maxSize = size;
    }
    /**
     *
     * This method gets the maxSIze for the selected leaderboard.
     *
     * @return the maxSize
     */
    public byte getMaxSize() {
        //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).maxSize;
        }
        //return selection as a fallback
        return this.maxSize;
    }
    /**
     * 
     * This method sets the invertList of the selected leaderboard
     * 
     * @param invert true to sort bottom to top
     */
    public void setInvertList(boolean invert) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).invertList = invert;
        }
        //set selection
        this.invertList = invert;
    }
    /**
     *
     * This method gets the invertList for the selected leaderboard.
     *
     * @return the invertList
     */
    public boolean getInvertList() {
        //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).invertList;
        }
        //return selection as a fallback
        return this.invertList;
    }
    /**
     * 
     * This method sets the invertList of the selected leaderboard
     * 
     * @param summon true to set hologram as summoned
     */
    public void setSummoned(boolean summon) {
        //set instance list
        if (boards.contains(this)) {
            boards.get(this.index).summoned = summon;
        }
        //set selection
        this.summoned = summon;
    }
    /**
     *
     * This method gets the summoned for the selected leaderboard.
     *
     * @return the summoned
     */
    public boolean getSummoned() {
        //return instance list
        if (boards.contains(this)) {
            return boards.get(this.index).summoned;
        }
        //return selection as a fallback
        return this.summoned;
    }
    /**
     * 
     * This method sets the name of the selected leaderboard
     * If the name is null, it won't apply (null names break selections)
     * 
     * @param pluginn the name to set
     */
    public void setPlugin(Plugin pluginn) {
        //set plugin in list
        if (boards.contains(this)) {
            boards.get(this.index).plugin = pluginn;
        }
        //set selected plugin
        this.plugin = pluginn;
    }
    /**
     *
     * This method gets the plugin for the selected leaderboard.
     *
     * @return the plugin
     */
    public Plugin getPlugin() {
        //return instance plugin
        if (boards.contains(this)) {
            return boards.get(this.index).plugin;
        }
        //return selected plugin as a fallback
        return this.plugin;
    }
}