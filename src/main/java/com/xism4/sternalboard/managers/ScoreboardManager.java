package com.xism4.sternalboard.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.api.scoreboard.SternalBoard;
import com.xism4.sternalboard.utils.placeholders.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class ScoreboardManager {

    private final Structure core;
    private final ConcurrentMap<UUID, SternalBoard> boards = new ConcurrentHashMap<>();
    private Integer[] taskIds;

    public ScoreboardManager(Structure core) {
        this.core = core;
    }

    public ConcurrentMap<UUID, SternalBoard> getBoards(){
        return this.boards;
    }

    public void init() {
        taskIds = new Integer[2];


        int updateTime;
        if(core.getConfig().getString("settings.mode").equalsIgnoreCase("WORLD")){
            if (core.getConfig().getInt("settings.scoreboard-world-update") <= 0) {
                core.getConfig().set("settings.scoreboard-world-update", 20);
            }
            updateTime = core.getConfig().getInt("settings.scoreboard-world-update");
        }else {
            if (core.getConfig().getInt("settings.scoreboard-normal-update") <= 0) {
                core.getConfig().set("settings.scoreboard-normal-update", 20);
            }
            updateTime = core.getConfig().getInt("settings.scoreboard-normal-update");
        }

        if (!core.isAnimationEnabled()){
            taskIds[0] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
                for (SternalBoard board : this.boards.values()) {
                    updateBoard(board);
                }
            }, 0, updateTime)).getTaskId();

            if (updateTime > 0) {
                taskIds[1] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {

                    for (SternalBoard board : this.boards.values()) {
                        if(core.getConfig().getString("settings.mode").equalsIgnoreCase("WORLD")){
                            if(core.getConfig().contains("scoreboard-world")){
                                for(String key : core.getConfig().getConfigurationSection("scoreboard-world").getKeys(false)){
                                    String nameWorld = core.getConfig().getString("scoreboard-world."+key+".world");
                                    if(nameWorld != null || !nameWorld.isEmpty()){
                                        if(board.getPlayer().getWorld().getName().equals(nameWorld)){
                                            board.updateTitle(PlaceholderUtils.sanitizeString(board.getPlayer(), core.getConfig().getString("scoreboard-world."+key+".title")));
                                        }
                                    }
                                }
                            }
                        }else{
                            board.updateTitle(PlaceholderUtils.sanitizeString(board.getPlayer(), core.getConfig().getString("settings.scoreboard.title")));
                        }
                    }
                }, 0, updateTime)).getTaskId();
            }
        }
    }

    public void setScoreboard(Player player) {
        SternalBoard board = new SternalBoard(player);
        getBoards().put(player.getUniqueId(), board);
        if (!core.isAnimationEnabled() && core.getConfig().getInt("settings.scoreboard.update") == 0) {
            if(core.getConfig().getString("settings.mode").equalsIgnoreCase("WORLD")){
                if(core.getConfig().contains("scoreboard-world")){
                    for(String key : core.getConfig().getConfigurationSection("scoreboard-world").getKeys(false)){
                        String nameWorld = core.getConfig().getString("scoreboard-world."+key+".world");
                        if(nameWorld != null || !nameWorld.isEmpty()){
                            if(player.getWorld().getName().equals(nameWorld)){
                                board.updateTitle(PlaceholderUtils.sanitizeString(player, core.getConfig().getString("scoreboard-world."+key+".title")));
                            }
                        }

                    }
                }
            }else{
                board.updateTitle(PlaceholderUtils.sanitizeString(player, core.getConfig().getString("settings.scoreboard.title")));
            }
        }
    }

    public void removeScoreboard(Player player) {
        SternalBoard board = getBoards().remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public void reload(){
        for (Integer taskId : taskIds){
            if (taskId != null){
                Bukkit.getServer().getScheduler().cancelTask(taskId);
            }
        }

        if (core.isAnimationEnabled() && taskIds[0] != null){
            for (SternalBoard board : this.boards.values()) {
                board.updateLines("");
            }
        }
        init();
    }

    public void toggle(Player player) {
        SternalBoard oldBoard = getBoards().remove(player.getUniqueId());
        if (oldBoard != null) {
            oldBoard.delete();
        }else{
            setScoreboard(player);
        }
    }

    private void updateBoard(SternalBoard board) {
        List<String> lines = new ArrayList<>();
        if(core.getConfig().getString("settings.mode").equalsIgnoreCase("WORLD")){
            if(core.getConfig().contains("scoreboard-world")){
                for(String key : core.getConfig().getConfigurationSection("scoreboard-world").getKeys(false)){
                    String nameWorld = core.getConfig().getString("scoreboard-world."+key+".world");
                    if(nameWorld != null || !nameWorld.isEmpty()) {
                        if (!board.getPlayer().getWorld().getName().equals(nameWorld)) continue;

                        lines = core.getConfig().getStringList("scoreboard-world." + key + ".lines");

                    }
                }
            }
        }else {
            lines = core.getConfig().getStringList("settings.scoreboard.lines");
        }

        if(lines.isEmpty() || lines == null){
            lines.add("No detect lines, notify staff!");
        }
        lines.replaceAll(s -> PlaceholderUtils.sanitizeString(board.getPlayer(), s));
        board.updateLines(lines);
    }
}
