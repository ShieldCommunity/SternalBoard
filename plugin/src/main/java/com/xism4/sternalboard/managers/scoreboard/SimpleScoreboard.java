package com.xism4.sternalboard.managers.scoreboard;

import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.managers.IScoreboard;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleScoreboard implements IScoreboard {

    private final Structure core;
    private final ConcurrentMap<UUID, SternalBoard> boards = new ConcurrentHashMap<>();
    private Integer[] taskIds;

    public SimpleScoreboard(Structure core) {
        this.core = core;
    }

    public ConcurrentMap<UUID, SternalBoard> getBoards() {
        return this.boards;
    }

    @Override
    public void run() {
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
            for (SternalBoard board : this.boards.values()) {
                updateScoreboard(board);
            }
        }, 0, updateTime)).getTaskId();

        taskIds[1] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
            for (SternalBoard board : this.boards.values()) {
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

    @Override
    public void setScoreboard(Player player) {
        SternalBoard board = new SternalBoard(player);
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

    @Override
    public void removeScoreboard(Player player) {
        SternalBoard board = getBoards().remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    @Override
    public void reloadScoreboard() {
        for (Integer taskId : taskIds) {
            if (taskId != null) {
                Bukkit.getServer().getScheduler().cancelTask(taskId);
            }
        }

        if (core.isAnimationEnabled() && taskIds[0] != null) {
            for (SternalBoard board : this.boards.values()) {
                board.updateLines("");
            }
        }
        run();
    }

    @Override
    public void updateScoreboard(SternalBoard board) {
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

    @Override
    public void toggleScoreboard(Player player) {
        SternalBoard oldBoard = getBoards().remove(player.getUniqueId());
        if (oldBoard != null) {
            oldBoard.delete();
        } else {
            setScoreboard(player);
        }

    }
}
