package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles team management for IMAPI
 * 
 */
public class Team {
    /**
     *
     * This constructor blocks new() usage
     * This forces usage of create() message
     * 
     */
    private Team() {}
    //team vars
    private String name = null;
    private List<IngotPlayer> members = new ArrayList<>();
    private int[][] baseLocation = new int[3][3];
    private byte maxSize = 0;
    private boolean friendlyFire = false;
    private boolean baseProtectedOutside = true;
    private boolean baseProtectedInside = false;
    private Plugin plugin = null;
    private int index = 0;
    //global vars
    private static List<Team> teams = new ArrayList<>();
    private static int trueIndex = 0;
    private static Plugin staticPlugin = Main.getInstance();
    private static FileConfiguration config = FileManager.getCustomData(staticPlugin, "config", "");
    /**
     *
     * This method creates a new team. 
     *
     * @param namee The spawn's name
     * @param x1 negative-most x pos
     * @param y1 negative-most y pos
     * @param z1 negative-most z pos
     * @param x2 positive-most x pos
     * @param y2 positive-most y pos
     * @param z2 positive-most z pos
     * @param memberss the list of members in the team
     * @param maxSizee the maxSize of players
     * @param friendlyFiree true to allow teammates to hurt each other
     * @param baseProtectionOutsidee true to have your base protected by outsiders
     * @param baseProtectionInsidee true to have your base protected by teammates
     * @param pluginn the plugin to attach to
     * @return the generated team
     */
    public static Team createTeam(String namee, int x1, int y1, int z1, int x2, int y2, int z2, List<IngotPlayer> memberss, byte maxSizee, boolean friendlyFiree, boolean baseProtectionOutsidee, boolean baseProtectionInsidee, Plugin pluginn) {
        //create spawn object
        Team newTeam = new Team();
        //create baselocaiton array
        newTeam.baseLocation[0][0] = x1;
        newTeam.baseLocation[1][0] = y1;
        newTeam.baseLocation[2][0] = z1;
        newTeam.baseLocation[0][0] = x2;
        newTeam.baseLocation[0][1] = y2;
        newTeam.baseLocation[0][2] = z2;
        //save variables
        newTeam.name = namee;
        newTeam.index = trueIndex;
        newTeam.plugin = pluginn;
        //return spawn
        teams.add(newTeam);
        trueIndex++;
        return newTeam;
    }
    /**
     *
     * This method deletes the selected team. 
     *
     */
    public void deleteTeam() {
        //decrement all higher indexes to prevent bugs
        for (Team key : teams) {
            if (key.index > this.index) {
                teams.get(key.index).index--;
            }
        }
        //reset variables
        this.name = null;
        this.baseLocation = null;
        this.members = null;
        this.maxSize = 0;
        this.friendlyFire = false;
        this.baseProtectedOutside = false;
        this.baseProtectedInside = false;
        this.index = 0;
        this.plugin = null;
        //remove team from instance list
        trueIndex--;
        teams.remove(trueIndex);
    }
    /**
     *
     * This method selects a given team if it exists
     *
     * @param namee the spawn to search for
     * @param pluginn the plugin to ensure its attached to
     * @return the selected spawn
     */
    public static Team selectTeam(String namee, Plugin pluginn) {
        //cycle through all loaded instances
        for (Team key : teams) {
            //check if spawn name is not null
            if (key.name != null) {
                //check if spawn name if equal to namee
                if (key.name.equals(namee) && key.plugin == pluginn) {
                    //return selection
                    return key;
                }
            }
        }
        //output error message
        if (config.getBoolean("enable-debug-mode") == true) {
            Logger.getLogger(Spawn.class.getName()).log(Level.SEVERE, null, "COULD NOT LOAD SPAWN " + namee + '!');
        }
        //return null
        return null;
    }
    /**
     * 
     * This method gets all loaded instances
     * Instances with null names are ignored (they shouldn't exist)
     * 
     * @return The list of teams
     */
    public static List<Team> getInstances() {
        //local vars
        List<Team> teamss = new ArrayList<>();
        //cycle through all spwans
        for (Team key : teams) {
            //check if spawn name isnt null
            if (key.name != null) {
                //add spawn
                teamss.add(key);
            }
            else {
                //delete invalid spawn
                key.deleteTeam();
            }
        }
        //return list
        return teamss;
    }
    /**
     * 
     * This method checks if 2 players are on this team
     * Good for any team-based different actions
     * 
     * @param iplayer1 first player to check
     * @param iplayer2 second player to check
     * @return true if on the same team
     */
    public boolean isOnSameTeam(IngotPlayer iplayer1, IngotPlayer iplayer2) {
        return (this.members.contains(iplayer1) && this.members.contains(iplayer2));
    }
    /**
     *
     * This method adds a player to the selected team. 
     *
     * @param iplayer the ingotplayer to add
     */
    public void addPlayer(IngotPlayer iplayer) {
        //check if team isnt full
        if (this.getMembers().size() < this.getMaxSize()) {
            //add instance list
            if (teams.contains(this)) {
                teams.get(this.index).members.add(iplayer);
            }
            //add selection
            this.members.add(iplayer);
            iplayer.setTeam(this, false);
        }
    }
    /**
     *
     * This method removes a player to the selected team. 
     *
     * @param iplayer the ingotplayer to remove
     */
    public void removePlayer(IngotPlayer iplayer) {
        //add instance list
        if (teams.contains(this)) {
            teams.get(this.index).members.remove(iplayer);
        }
        //add selection
        this.members.remove(iplayer);
        iplayer.setTeam(null, false);
    }
    /**
     *
     * This method sets the name of the selected team. 
     * setting to null is not allowed since it breaks selections
     *
     * @param namee The name to set
     */
    public void setName(String namee) {
        //check if name is null
        if (namee != null) {
            //set instance list
            if (teams.contains(this)) {
                teams.get(this.index).name = namee;
            }
            //set selection
            this.name = namee;
        }
        else {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SET THE NAME FOR ARENA " + this.name + '!');
            }
        }
    }
    /**
     *
     * This method obtains the name of the selected spawn. 
     *
     * @return the team name
     */
    public String getName() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).name;
        }
        //return selection as a fallback
        return this.name;
    }
    /**
     *
     * This method sets the plugin of the selected team 
     * If there are more members than the maxsize, the ones
     * outside of range will be removed. Ensure maxSize is always
     * greater than or equal to the member list size
     *
     * @param memberss the players to set
     */
    public void setMembers(List<IngotPlayer> memberss) {
        //cut members outside of range
        if (memberss.size() > this.maxSize) {
            //cycle thorugh all members outside of range
            for (int i=this.maxSize; i < memberss.size(); i++) {
                //remove member
                memberss.remove(i);
            }
        }
        //set instance array
        if (teams.contains(this)) {
            teams.get(this.index).members = memberss;
        }
        //set selection
        this.members = memberss;
    }
    /**
     *
     * This method obtains the plugin of the selected team
     *
     * @return the member list
     */
    public List<IngotPlayer> getMembers() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).members;
        }
        //return selection as a fallback
        return this.members;
    }
    /**
     *
     * This method sets the maxSize of the selected team 
     *
     * @param maxSizee the maxSize
     */
    public void setMaxSize(byte maxSizee) {
        //set instance array
        if (teams.contains(this)) {
            teams.get(this.index).maxSize = maxSizee;
        }
        //set selection
        this.maxSize = maxSizee;
    }
    /**
     *
     * This method obtains the maxSize of the selected team
     *
     * @return the maxSize
     */
    public byte getMaxSize() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).maxSize;
        }
        //return selection as a fallback
        return this.maxSize;
    }
    /**
     *
     * This method sets the friendlyFire of the selected team 
     *
     * @param firee the friendlyFire state
     */
    public void setFriendlyFire(boolean firee) {
        //set instance array
        if (teams.contains(this)) {
            teams.get(this.index).friendlyFire = firee;
        }
        //set selection
        this.friendlyFire = firee;
    }
    /**
     *
     * This method obtains the plugin of the selected team
     *
     * @return the friendlyfire state
     */
    public boolean getFriendlyFire() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).friendlyFire;
        }
        //return selection as a fallback
        return this.friendlyFire;
    }
    /**
     *
     * This method sets the baseProtectedOutside of the selected team 
     *
     * @param basee the baseProtectedOutside state
     */
    public void setBaseProtectedOutside(boolean basee) {
        //set instance array
        if (teams.contains(this)) {
            teams.get(this.index).baseProtectedOutside = basee;
        }
        //set selection
        this.baseProtectedOutside = basee;
    }
    /**
     *
     * This method obtains the baseProtectedOutside of the selected team
     *
     * @return the baseProtectedOutside state
     */
    public boolean getBaseProtectedOutside() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).baseProtectedOutside;
        }
        //return selection as a fallback
        return this.baseProtectedOutside;
    }
    /**
     *
     * This method sets the baseProtectedInside of the selected team 
     *
     * @param basee the baseProtectedInside state
     */
    public void setBaseProtectedInside(boolean basee) {
        //set instance array
        if (teams.contains(this)) {
            teams.get(this.index).baseProtectedInside = basee;
        }
        //set selection
        this.baseProtectedInside = basee;
    }
    /**
     *
     * This method obtains the baseProtectedInside of the selected team
     *
     * @return the baseProtectedInside state
     */
    public boolean getBaseProtectedInside() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).baseProtectedInside;
        }
        //return selection as a fallback
        return this.baseProtectedInside;
    }
    /**
     *
     * This method sets the plugin of the selected team 
     *
     * @param pluginn the plugin state
     */
    public void setPlugin(Plugin pluginn) {
        //set instance array
        if (teams.contains(this)) {
            teams.get(this.index).plugin = pluginn;
        }
        //set selection
        this.plugin = pluginn;
    }
    /**
     *
     * This method obtains the plugin of the selected team
     *
     * @return the plugin state
     */
    public Plugin getPlugin() {
        //return instance list
        if (teams.contains(this)) {
            return teams.get(this.index).plugin;
        }
        //return selection as a fallback
        return this.plugin;
    }
}
