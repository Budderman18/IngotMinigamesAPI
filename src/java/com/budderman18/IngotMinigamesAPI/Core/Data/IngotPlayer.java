package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * This class handles all playerdata managed by 
 * IMAPI plugins. Currently, its limited to 
 * username, inGame, isPlaying, isFrozen and game. 
 * 
 */
public class IngotPlayer {
    //player vars
    private String username = null;
    private boolean inGame = false;
    private boolean isPlaying = false;
    private boolean isFrozen = false;
    private boolean isGraced = false;
    private boolean isAlive = true;
    private boolean isAttacked = false;
    private byte gameKills = 0;
    private short score = 0;
    private short wins = 0;
    private short losses = 0;
    private short kills = 0;
    private short deaths = 0;
    private String game = null;
    private ItemStack[] inventory = null;
    private PotionEffect[] effects = null;
    private float[] xp = new float[2];
    private float[] health = new float[2];
    private Team team = null;
    private Plugin plugin = null;
    private int index;
    //global vars
    private static List<IngotPlayer> players = new ArrayList<>();
    private static int trueIndex = 0;
    private static Plugin staticPlugin = Main.getInstance();
    private static FileConfiguration config = FileManager.getCustomData(staticPlugin, "config", "");
    /**
     *
     * This constructor does absolutely nothing.
     * If you want to create new iplayers, use create() method!
     * This is only public to allow extentions
     *
     */
    @Deprecated
    public IngotPlayer() {}
    /**
     *
     * This method creates a new player for IMAPI. 
     * 
     * @param player The player to attach to
     * @param inGamee true if in lobby or game
     * @param isPlayingg true if in game
     * @param isFrozenn true if unable to move
     * @param isGracedd true if invincible
     * @param isAlivee true if alive
     * @param gameKillss the amount of kills in a game
     * @param killss the kill count
     * @param deathss the death count
     * @param winss the win count
     * @param lossess the loss count
     * @param scoree the score count
     * @param gamee the game they are in
     * @param pluginn The plugin to attach to
     * @return the player object created
     */
    public static IngotPlayer createPlayer(String player, boolean inGamee, boolean isPlayingg, boolean isFrozenn, boolean isGracedd, boolean isAlivee, boolean isAttackedd, byte gameKillss, short killss, short deathss, short winss, short lossess, short scoree, String gamee, Plugin pluginn) {
        //create new iplayer
        IngotPlayer newPlayer = new IngotPlayer();
        //set vars
        newPlayer.username = player;
        newPlayer.inGame = inGamee;
        newPlayer.isPlaying = isPlayingg;
        newPlayer.isFrozen = isFrozenn;
        newPlayer.isGraced = isGracedd;
        newPlayer.isAlive = isAlivee;
        newPlayer.isAttacked = isAttackedd;
        newPlayer.gameKills = gameKillss;
        newPlayer.wins = winss;
        newPlayer.losses = lossess;
        newPlayer.kills = killss;
        newPlayer.deaths = deathss;
        newPlayer.score = scoree;
        newPlayer.game = gamee;
        if (Bukkit.getPlayer(player) != null) {
            newPlayer.inventory = Bukkit.getPlayer(player).getInventory().getContents();
            newPlayer.xp[0] = Bukkit.getPlayer(player).getExp();
            newPlayer.xp[1] = Bukkit.getPlayer(player).getLevel();
        }
        newPlayer.index = trueIndex;
        newPlayer.plugin = pluginn;
        //add to list
        players.add(newPlayer);
        trueIndex++;
        //return player
        return newPlayer;
    }
    /**
     *
     * This method deletes a player from IMAPI. 
     *
     */
    public void deletePlayer() {
        //decrement all higher indexes to prevent bugs
        for (IngotPlayer key : players) {
            if (key.index > this.index) {
                players.get(key.index).index--;
            }
        }
        //delete from list
        players.remove(this.index);
        trueIndex--;
        //delete vars
        this.username = null;
        this.inGame = false;
        this.isPlaying = false;
        this.isFrozen = false;
        this.isGraced = false;
        this.isAlive = false;
        this.isAttacked = false;
        this.wins = 0;
        this.losses = 0;
        this.kills = 0;
        this.deaths = 0;
        this.game = null;
        this.inventory = null;
        this.xp = null;
        this.index = 0;
        this.plugin = null;
    }
    /**
     *
     * This method selects a given ingotplayer using their username. 
     *
     * @param namee the name to search for
     * @param pluginn the plugin it should be attached to
     * @return the selected player object
     */
    public static IngotPlayer selectPlayer(String namee, Plugin pluginn) {
        //cycle through all loaded instances
        for (IngotPlayer key : players) {
            //check if username is not null
            if (key.getUsername() != null) {
                //check if selected iplayer's name is equal to namee
                if (key.getUsername().equals(namee) && key.getPlugin() == pluginn) {
                    //return iplayer
                    return key;
                }
            }
        }
        //output error message and return null
        if (config.getBoolean("enable-debug-mode") == true) {
            Logger.getLogger(IngotPlayer.class.getName()).log(Level.SEVERE, "COULD NOT LOAD PLAYER " + namee + '!');
        }
        return null;
    }
    /**
     * 
     * This method gets all loaded instances
     * Instances with null names are ignored (they shouldn't exist)
     * 
     * @return The list of players
     */
    public static List<IngotPlayer> getInstances() {
        //local vars
        List<IngotPlayer> playerss = new ArrayList<>();
        //cycle through list
        for (IngotPlayer key : players) {
            //check if username is null
            if (key.username != null) {
                //add player
                playerss.add(key);
            }
            else {
                //delete player object so it can't show up later
                key.deletePlayer();
            }    
        }
        //return list
        return playerss;
    }
    /**
     * 
     * THis method saves the selected player to the file
     * 
     */
    public void saveToFile() {
        //local vars
        File playerdataf = new File(this.plugin.getDataFolder(), "playerdata.yml");
        FileConfiguration playerdata = FileManager.getCustomData(this.plugin, "playerdata", "");
        //set file
        playerdata.set(this.username + ".kills", this.kills);
        playerdata.set(this.username + ".deaths", this.deaths);
        playerdata.set(this.username + ".wins", this.wins);
        playerdata.set(this.username + ".losses", this.losses);
        playerdata.set(this.username + ".score", this.score);
        //save file
        try {
            playerdata.save(playerdataf);
        } 
        catch (IOException ex) {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(IngotPlayer.class.getName()).log(Level.SEVERE, "COULD NOT SAVE PLAYERDATA.YML!");
            }
        }
    }
    /**
     * 
     * This method sets the selected player's inventory using their iplayer's inventory
     * You can also set experience as well
     * 
     * @param useEffects true to apply potions
     * @param useXP true to apply stored xp
     * @param useHP true to apply hp
     */
    public void applyInventory(boolean useEffects, boolean useXP, boolean useHP) {
        //local vars
        byte indexx = 0;
        //cyclethrough inentory
        for (ItemStack key : this.inventory) {
            //check if item is valid
            if (key != null) {
                //set item
                Bukkit.getPlayer(this.username).getInventory().setItem(indexx, key);
            }
            //increment index
            indexx++;
        }
        //check if using effects
        if (useEffects == true) {
            //cycle through effects
            for (PotionEffectType key : PotionEffectType.values()) {
                //remove effect
                Bukkit.getPlayer(this.username).removePotionEffect(key);
            }
            //cycle through effects
            for (PotionEffect key : this.effects) {
                //check if effect isnt null
                if (key != null) {
                    //add effect
                    Bukkit.getPlayer(this.username).addPotionEffect(key);
                }
            }
        }
        //check if using xp
        if (useXP == true) {
            //set xp and level
            Bukkit.getPlayer(this.username).setExp(xp[0]);
            Bukkit.getPlayer(this.username).setLevel((int) xp[1]);
        }
        //check if using hp
        if (useHP == true) {
            //set xp and level
            Bukkit.getPlayer(this.username).setHealth(this.health[0]);
            Bukkit.getPlayer(this.username).setFoodLevel((int) this.health[1]);
        }
    }
    /**
     * 
     * This method clears the selected player's inventory
     * this does NOT touch the stored inventory of this object, just their current inventory
     * You can also set experience as well
     * 
     * @param useEffects true to use potions
     * @param useXP true to clear stored xp
     * @param useHP true to set HP to full
     */
    public void clearInventory(boolean useEffects, boolean useXP, boolean useHP) {
        //clear inventory
        Bukkit.getPlayer(this.username).getInventory().clear();
        //check if using effects
        if (useEffects == true) {
            //cycle trhough effects
            for (PotionEffectType key : PotionEffectType.values()) {
                //remove effects
                Bukkit.getPlayer(this.username).removePotionEffect(key);
            }
        }
        //check if using xp
        if (useXP == true) {
            //reset xp and lever
            Bukkit.getPlayer(this.username).setExp(0);
            Bukkit.getPlayer(this.username).setLevel(0);
        }
        //check if using hp
        if (useHP == true) {
            //set xp and level
            Bukkit.getPlayer(this.username).setHealth( Bukkit.getPlayer(this.username).getMaxHealth());
            Bukkit.getPlayer(this.username).setFoodLevel(20);
        }
    }
    /**
     *
     * This method sets the username of the selected iplayer. 
     * null setting isblocked to prevent selection issues
     *
     * @param usernamee the username to set
     */
    public void setUsername(String usernamee) {
        //check if name is null
        if (usernamee != null) {
            //set instance list
            if (players.contains(this)) {
                players.get(this.index).username = usernamee;
            }
            //set selection
            this.username = usernamee;
        }
        else {
            //check for debug mode
            if (config.getBoolean("enable-debug-mode") == true) {
                //log error
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SET THE NAME FOR ARENA " + this.username + '!');
            }
        }
    }
    /**
     *
     * This method obtains the username of the current player. 
     *
     * @return the player's username
     */
    public String getUsername() {
        //return instance list
        if (players.contains(this)) {
             return players.get(this.index).username;
        }
        //return selection as a fallback
        return this.username;
    }
    /**
     *
     * This method changes the inventory of the current player. 
     *
     * @param inventoryy the inventory objectto set
     */
    public void setInventory(ItemStack[] inventoryy) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).inventory = inventoryy;
        }
        //set selection
        this.inventory = inventoryy;
    }
    /**
     *
     * This method gets the inventory of the current player. 
     *
     * @return the inventory object
     */
    public ItemStack[] getInventory() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).inventory;
        }
        //return selection as a fallback
        return this.inventory;
    }
    /**
     *
     * This method changes the inventory of the current player. 
     *
     * @param effectss the effect array to set
     */
    public void setEffects(PotionEffect[] effectss) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).effects = effectss;
        }
        //set selection
        this.effects = effectss;
    }
    /**
     *
     * This method gets the inventory of the current player. 
     *
     * @return the effect array
     */
    public PotionEffect[] getEffects() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).effects;
        }
        //return selection as a fallback
        return this.effects;
    }
    /**
     *
     * This method changes the xp for the selected player
     * [0] is exp and [1] is level
     *
     * @param xpp the xp array to set
     */
    public void setXP(float[] xpp) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).xp = xpp;
        }
        //set selection
        this.xp = xpp;
    }
    /**
     *
     * This method gets the xp of the current player. 
     * [0] is exp and [1] is level
     *
     * @return the xp array
     */
    public float[] getXP() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).xp;
        }
        //return selection as a fallback
        return this.xp;
    }
    /**
     *
     * This method changes the hp for the selected player
     * [0] is hp and [1] is hunger
     *
     * @param hpp the hp array to set
     */
    public void setHealth(float[] hpp) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).health = hpp;
        }
        //set selection
        this.health = hpp;
    }
    /**
     *
     * This method gets the hp of the current player. 
     * [0] is health and [1] is hunger
     *
     * @return the hp array
     */
    public float[] getHealth() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).health;
        }
        //return selection as a fallback
        return this.health;
    }
    /**
     *
     * This method changes the team for the selected player
     *
     * @param teamm the team to set
     * @param addToTeam true to run team.addPlayer()
     */
    public void setTeam(Team teamm, boolean addToTeam) {
        //check if team contains this and addToTeam is true
        if (addToTeam == true) {
            //check if team isnt null
            if (this.team != null) {
                //remove from old team
                this.team.removePlayer(this);
            }
            //add to new team
            this.team = teamm;
            this.team.addPlayer(this);
        }
        else {
            //check if team isnt null
            if (teamm != null) {
                //check if team can fit another member
                if (teamm.getMembers().size() < teamm.getMaxSize()) {
                    //set instance list
                    if (players.contains(this)) {
                        players.get(this.index).team = teamm;
                    }
                    //set selection
                    this.team = teamm;
                }
            }
            else {
                // set instance list
                if (players.contains(this)) {
                    players.get(this.index).team = teamm;
                }
                //set selection
                this.team = teamm;
            }
        }
    }
    /**
     *
     * This method gets the xp of the current player. 
     *
     * @return the team
     */
    public Team getTeam() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).team;
        }
        //return selection as a fallback
        return this.team;
    }
    /**
     *
     * This method changes the inGame of the current player. 
     *
     * @param inGamee the boolean to set
     */
    public void setInGame(boolean inGamee) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).inGame = inGamee;
        }
        //set selection
        this.inGame = inGamee;
    }
    /**
     *
     * This method gets the inGame of the current player. 
     *
     * @return the inGame state
     */
    public boolean getInGame() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).inGame;
        }
        //return selection as a fallback
        return this.inGame;
    }
    /**
     *
     * This method changes the isPlaying of the current player. 
     *
     * @param isPlayingg the isPlaying state
     */
    public void setIsPlaying(boolean isPlayingg) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).isPlaying = isPlayingg;
        }
        //set selection
        this.isPlaying = isPlayingg;
    }
    /**
     *
     * This method gets the isPlaying of the current player. 
     *
     * @return the inGame state
     */
    public boolean getIsPlaying() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).isPlaying;
        }
        //return selection as a fallback
        return this.isPlaying;
    }
    /**
     *
     * This method changes the isFrozen of the current player. 
     *
     * @param isFrozenn the isFrozen state
     */
    public void setIsFrozen(boolean isFrozenn) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).isFrozen = isFrozenn;
        }
        //set selection
        this.isFrozen = isFrozenn;
    }
    /**
     *
     * This method gets the frozen state of the selected player. 
     *
     * @return the isFrozen state
     */
    public boolean getIsFrozen() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).isFrozen;
        }
        //return selection as a fallback
        return this.isFrozen;
    }
    /**
     *
     * This method sets the isAlive for the selected player. 
     *
     * @param isAlivee the isAlive state
     */
    public void setIsAlive(boolean isAlivee) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).isAlive = isAlivee;
        }
        //set selection
        this.isAlive = isAlivee;
    }
    /**
     *
     * This method sets the isAlive for the selected player.  
     *
     * @return the isAlive state
     */
    public boolean getIsAlive() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).isAlive;
        }
        //return selection as a fallback
        return this.isAlive;
    }
    /**
     *
     * This method sets the isGraced for the selected player. 
     *
     * @param isGracedd the isGraced state
     */
    public void setIsGraced(boolean isGracedd) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).isGraced = isGracedd;
        }
        //set selection
        this.isGraced = isGracedd;
    }
    /**
     *
     * This method gets the isGraced for the selected player. 
     *
     * @return the isGraced state
     */
    public boolean getIsGraced() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).isGraced;
        }
        //return selection as a fallback
        return this.isGraced;
    }
    /**
     *
     * This method sets the isAttacked for the selected player. 
     *
     * @param isAttackedd the isAttacked state
     */
    public void setIsAttacked(boolean isAttackedd) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).isAttacked = isAttackedd;
        }
        //set selection
        this.isAttacked = isAttackedd;
    }
    /**
     *
     * This method gets the isAttacked for the selected player. 
     *
     * @return the isAttacked state
     */
    public boolean getIsAttacked() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).isAttacked;
        }
        //return selection as a fallback
        return this.isAttacked;
    }
    /**
     *
     * This method changes the game the selected player is in. 
     *
     * @param gamee The arena name to set
     */
    public void setGame(String gamee) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).game = gamee;
        }
        //set selection
        this.game = gamee;
    }
    /**
     *
     * This method gets the current game the player is in. 
     *
     * @return the arena name
     */
    public String getGame() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).game;
        }
        //returnselection as a fallback
        return this.game;
    }
    /**
     *
     * This method changes the plugin the selected player is in. 
     *
     * @param pluginn the plugin object
     */
    public void setPlugin(Plugin pluginn) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).plugin = pluginn;
        }
        //set selection
        this.plugin = pluginn;
    }
    /**
     *
     * This method gets the plugin the player was made for.
     *
     * @return the plugin object the player is attached to
     */
    public Plugin getPlugin() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).plugin;
        }
        //return selection as a fallback
        return this.plugin;
    }
    /**
     *
     * This method sets the amount of wins the player has 
     *
     * @param winss the win amount
     */
    public void setWins(short winss) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).wins = winss;
        }
        //set selection
        this.wins = winss;
    }
    /**
     *
     * This method gets the amount of wins the player has
     *
     * @return the win amount
     */
    public short getWins() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).wins;
        }
        //return selection as a fallback
        return this.wins;
    }
    /**
     *
     * This method sets the amount of losses the player has
     *
     * @param lossess the loss amount
     */
    public void setLosses(short lossess) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).losses = lossess;
        }
        //set selection
        this.losses = lossess;
    }
    /**
     *
     * This method gets the amount of losses the player has
     *
     * @return the loss amount
     */
    public short getLosses() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).losses;
        }
        //return selection as a fallback
        return this.losses;
    }
    /**
     *
     * This method sets the amount of game kills the player has. 
     *
     * @param killss the kill amount
     */
    public void setGameKills(byte killss) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).gameKills = killss;
        }
        //set selection
        this.gameKills = killss;
    }
    /**
     *
     * This method gets the amount of kills the player has
     *
     * @return the kill amount
     */
    public byte getGameKills() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).gameKills;
        }
        //return selection as a fallback
        return this.gameKills;
    }
    /**
     *
     * This method sets the amount of kills the player has. 
     *
     * @param killss the kill amount
     */
    public void setKills(short killss) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).kills = killss;
        }
        //set selection
        this.kills = killss;
    }
    /**
     *
     * This method gets the amount of kills the player has
     *
     * @return the kill amount
     */
    public short getKills() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).kills;
        }
        //return selection as a fallback
        return this.kills;
    }
    /**
     *
     * This method sets the amount of deaths the player has 
     *
     * @param deathss the death amount
     */
    public void setDeaths(short deathss) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).deaths = deathss;
        }
        //set selection
        this.deaths = deathss;
    }
    /**
     *
     * This method gets the amount of deaths the player has
     *
     * @return the death amount
     */
    public short getDeaths() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).deaths;
        }
        //return selection as a fallback
        return this.deaths;
    }
    /**
     *
     * This method sets the amount of score the player has
     *
     * @param scoree the score amount
     */
    public void setScore(short scoree) {
        //set instance list
        if (players.contains(this)) {
            players.get(this.index).score = scoree;
        }
        //set selection
        this.score = scoree;
    }
    /**
     *
     * This method gets the amount of score the player has
     *
     * @return the score amount
     */
    public short getScore() {
        //return instance list
        if (players.contains(this)) {
            return players.get(this.index).score;
        }
        //return selection as a fallback
        return this.score;
    }
}