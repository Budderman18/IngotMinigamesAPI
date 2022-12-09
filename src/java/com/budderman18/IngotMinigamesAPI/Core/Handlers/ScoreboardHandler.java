package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 *
 * This class handles everything involving scoreboards
 * This includes titles, lines and clearing
 * 
 */
public class ScoreboardHandler {
    //global vars
    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static Scoreboard scoreboard = manager.getNewScoreboard();
    private static String title = "error";
    private static Objective objective = scoreboard.registerNewObjective("Scoreboard", "empty", title);
    private static Objective sidebar = null;
    private static Objective belowName = null;
    private static Objective list = null;
    /**
     * 
     * This constructor blocks new() usage as it does nothing
     * 
     */
    private ScoreboardHandler() {}
    /**
     * 
     * This method gets the main scoreboard.
     * It is used to update the temporary one with the server's objectives
     * 
     */
    private static void updateScoreboard() {
        //local vars
        Scoreboard mainScoreboard = manager.getMainScoreboard();
        //get objectives
        sidebar = mainScoreboard.getObjective(DisplaySlot.SIDEBAR);
        belowName = mainScoreboard.getObjective(DisplaySlot.BELOW_NAME);
        list = mainScoreboard.getObjective(DisplaySlot.PLAYER_LIST);
        //check if sidebar is empty
        if (scoreboard.getObjective(DisplaySlot.SIDEBAR) == null && mainScoreboard.getObjective(DisplaySlot.SIDEBAR) != null) {
            //set sidebar
            sidebar = scoreboard.registerNewObjective(sidebar.getName() + " sidebar", sidebar.getCriteria(), sidebar.getDisplayName());
        }
        else {
            //obtain sidebar
            sidebar = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        }
        //check if sidebar isnt null
        if (sidebar != null) {
            //create new sidebar
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            sidebar.setRenderType(RenderType.INTEGER);
        }
        //check if belowName is empty
        if (scoreboard.getObjective(DisplaySlot.BELOW_NAME) == null && mainScoreboard.getObjective(DisplaySlot.BELOW_NAME) != null) {
            //set belowname
            belowName = scoreboard.registerNewObjective(belowName.getName() + " belowName", belowName.getCriteria(), belowName.getDisplayName());
        }
        else {
            //obtain belowname
            belowName = scoreboard.getObjective(DisplaySlot.BELOW_NAME);
        }
        //check if belowName isnt null
        if (belowName != null) {
            //create new sidebar
            belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
            belowName.setRenderType(RenderType.INTEGER);
        }
        //check if playerlist isnt null
        if (scoreboard.getObjective(DisplaySlot.PLAYER_LIST) == null && mainScoreboard.getObjective(DisplaySlot.PLAYER_LIST) != null) {
            //set new list
            list = scoreboard.registerNewObjective(list.getName() + " list", list.getCriteria(), list.getDisplayName());
        }
        else {
            //obtain list
            list = scoreboard.getObjective(DisplaySlot.PLAYER_LIST);
        }
        //check if playerlist isnt null
        if (list != null) {
            //set new list
            list.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            list.setRenderType(RenderType.INTEGER);
        }
    }
    /**
     *
     * This method changes the designated line of the scoreboard
     *
     * @param player THe player to affect
     * @param line the line number
     * @param lineString the text to change to
     * @param includeMainScoreboard true to include the server's scoreboard data
     */
    public static void setLine(Player player, byte line, String lineString, boolean includeMainScoreboard) {
        //local vars
        Score score = null;
        //check if score does not exist
        if (objective.getScore(lineString).isScoreSet() == false) {
            //set score's line
            line = (byte) (16 - line);
            //set score
            score = objective.getScore(lineString); 
            score.setScore(line);
        }
        else {
            //get the current title
            title = objective.getDisplayName();
            //regenerate the scoreboard
            scoreboard = manager.getNewScoreboard();
            objective = scoreboard.registerNewObjective("Scoreboard", "empty", title);
            //set score's line
            line = (byte) (16 - line); 
            //set score
            score = objective.getScore(lineString);
            score.setScore(line);
        }
        //set display
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        //get main scoreboard's objectives 
        if (includeMainScoreboard == true) {
            updateScoreboard();
        }
        //send scoreboard
        player.setScoreboard(scoreboard);
    }
    /** 
     *
     * This method sets the title of the scoreboard
     *
     * @param player THe player to affect
     * @param string the text to change to
     * @param includeMainScoreboard true to include the server's scoreboard data
     */
    public static void setTitle(Player player, String string, boolean includeMainScoreboard) {
        //set display
        objective.setDisplayName(string);
        title = string;
        //get main scoreboard's objectives
        if (includeMainScoreboard == true) {
            updateScoreboard();
        }
        //send scoreboard
        player.setScoreboard(scoreboard);
    }
    /**
     *
     * This method deletes the scoreboard of a given player
     *
     * @param player the player to affect
     */
    public static void clearScoreboard(Player player) {
        //local vars
        Scoreboard mainScoreboard = manager.getMainScoreboard();
        //send scoreboard
        player.setScoreboard(mainScoreboard);
    }
}