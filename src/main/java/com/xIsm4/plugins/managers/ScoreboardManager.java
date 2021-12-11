package com.xIsm4.plugins.managers;

import java.util.List;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

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

        if (core.getConfig().getInt("settings.scoreboard.update") <= 0) {
            core.getConfig().set("settings.scoreboard.update", 20);
        }

        if (!core.isAnimationEnabled()){
            taskIds[0] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
                for (SternalBoard board : this.boards.values()) {
                    updateBoard(board);
                }
            }, 0, core.getConfig().getInt("settings.scoreboard.update", 20))).getTaskId();

            if (core.getConfig().getInt("settings.scoreboard.update") > 0) {
                taskIds[1] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
                    for (SternalBoard board : this.boards.values()) {
                        board.updateTitle(PlaceholderUtils.sanitizeString(board.getPlayer(), core.getConfig().getString("settings.scoreboard.title")));
                    }
                }, 0, core.getConfig().getInt("settings.scoreboard.update", 20))).getTaskId();
            }
        }
    }

    public void setScoreboard(Player player) {
        SternalBoard board = new SternalBoard(player);
        getBoards().put(player.getUniqueId(), board);

        if (!core.isAnimationEnabled() && core.getConfig().getInt("settings.scoreboard.update") == 0) {
            board.updateTitle(PlaceholderUtils.sanitizeString(player, core.getConfig().getString("settings.scoreboard.title")));
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
        List<String> lines = core.getConfig().getStringList("settings.scoreboard.lines");
        lines.replaceAll(s -> PlaceholderUtils.sanitizeString(board.getPlayer(), s));
        board.updateLines(lines);
    }
}
