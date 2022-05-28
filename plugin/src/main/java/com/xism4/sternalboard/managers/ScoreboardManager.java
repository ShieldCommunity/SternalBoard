package com.xism4.sternalboard.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class ScoreboardManager {

    private final SternalBoard core;
    private final ConcurrentMap<UUID, SternalBoardHandler> boards = new ConcurrentHashMap<>();
    private Integer[] taskIds;

    public ScoreboardManager(SternalBoard core) {
        this.core = core;
    }

    public ConcurrentMap<UUID, SternalBoardHandler> getBoards() {
        return this.boards;
    }

    public void init() {
        taskIds = new Integer[2];
        FileConfiguration config = core.getConfig();
        String mode = config.getString("settings.mode", "normal").toLowerCase();
        mode = mode.equals("normal") || mode.equals("world") ? mode : "normal";
        String boardPath = "settings.scoreboard-" + mode + "-update";
        int updateTime;

        if (config.getInt(boardPath) <= 0) {
            config.set(boardPath, 20);
            core.saveConfig();
        }

        updateTime = config.getInt(boardPath);

        if (core.isAnimationEnabled()) {
            return;
        }

        taskIds[0] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
            for (SternalBoardHandler board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, updateTime)).getTaskId();

        taskIds[1] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
            for (SternalBoardHandler board : this.boards.values()) {
                if (!config.getString("settings.mode").equalsIgnoreCase("WORLD")) {
                    board.updateTitle(PlaceholderUtils.sanitizeString(
                            board.getPlayer(), config.getString("settings.scoreboard.title")
                    ));
                    return;
                }

                if (!config.contains("scoreboard-world." + board.getPlayer().getWorld().getName())) {
                    board.updateTitle(PlaceholderUtils.sanitizeString(
                            board.getPlayer(), config.getString("settings.scoreboard.title")
                    ));
                    return;
                }

                board.updateTitle(PlaceholderUtils.sanitizeString(
                        board.getPlayer(),
                        config.getString("scoreboard-world." + board.getPlayer().getWorld().getName() + ".title")
                ));
            }
        }, 0, updateTime)).getTaskId();
    }

    public void setScoreboard(Player player) {
        SternalBoardHandler board = new SternalBoardHandler(player);
        FileConfiguration config = core.getConfig();
        getBoards().put(player.getUniqueId(), board);

        if (core.isAnimationEnabled() && config.getInt("settings.scoreboard.update") != 0) {
            return;
        }

        if (config.getString("settings.mode") == null) {
            config.set("settings.mode", "DEFAULT");
            core.saveConfig();
        }

        if (!config.getString("settings.mode").equalsIgnoreCase("WORLD")) {
            board.updateTitle(PlaceholderUtils.sanitizeString(
                    player, config.getString("settings.scoreboard.title")
            ));
            return;
        }

        if (!config.contains("scoreboard-world." + player.getWorld().getName())) {
            board.updateTitle(PlaceholderUtils.sanitizeString(
                    player, config.getString("settings.scoreboard.title")
            ));
            return;
        }

        board.updateTitle(PlaceholderUtils.sanitizeString(
                player, config.getString("scoreboard-world." + player.getWorld().getName() + ".title")
        ));
    }

    public void removeScoreboard(Player player) {
        SternalBoardHandler board = getBoards().remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public void reload() {
        for (Integer taskId : taskIds) {
            if (taskId != null) {
                Bukkit.getServer().getScheduler().cancelTask(taskId);
            }
        }

        if (core.isAnimationEnabled() && taskIds[0] != null) {
            for (SternalBoardHandler board : this.boards.values()) {
                board.updateLines("");
            }
        }
        init();
    }

    public void toggle(Player player) {
        SternalBoardHandler oldBoard = getBoards().remove(player.getUniqueId());
        if (oldBoard != null) {
            oldBoard.delete();
        } else {
            setScoreboard(player);
        }
    }

    private void updateBoard(SternalBoardHandler board) {
        List<String> lines = new ArrayList<>();
        if (!core.getConfig().getString("settings.mode").equalsIgnoreCase("WORLD")) {
            lines = core.getConfig().getStringList("settings.scoreboard.lines");
            lines.replaceAll(s -> PlaceholderUtils.sanitizeString(board.getPlayer(), s));
            board.updateLines(lines);
            return;
        }

        if (core.getConfig().contains("scoreboard-world." + board.getPlayer().getWorld().getName())) {
            lines = core.getConfig().getStringList(
                    "scoreboard-world." + board.getPlayer().getWorld().getName() + ".lines"
            );
        }

        if (lines.isEmpty()) {
            lines = core.getConfig().getStringList("settings.scoreboard.lines");
        }
        lines.replaceAll(line -> PlaceholderUtils.sanitizeString(board.getPlayer(), line));
        board.updateLines(lines);
    }
}
