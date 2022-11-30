package com.xism4.sternalboard.managers;

import com.xism4.sternalboard.Scoreboards;
import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ScoreboardManager {

    private final SternalBoard core;
    private final ConcurrentMap<UUID, SternalBoardHandler> boards = new ConcurrentHashMap<>();
    private Integer updateTask;

    public ScoreboardManager(SternalBoard core) {
        this.core = core;
    }

    public ConcurrentMap<UUID, SternalBoardHandler> getBoards() {
        return this.boards;
    }

    public void init() {
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

        updateTask = core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {

            ConfigurationSection defaultSection = core.getConfig().getConfigurationSection("settings.scoreboard");

            for (SternalBoardHandler board : this.boards.values()) {
                if (!Objects.requireNonNull(config.getString("settings.mode")).equalsIgnoreCase("WORLD")) {

                    Scoreboards.updateFromSection(board, defaultSection);
                    return;
                }

                String worldName = board.getPlayer().getWorld().getName();
                ConfigurationSection worldSection = core.getConfig().getConfigurationSection("scoreboard-world." + worldName);

                if (worldSection == null) {
                    Scoreboards.updateFromSection(board, defaultSection);
                    return;
                }

                Scoreboards.updateFromSection(board, worldSection);
            }
        }, 0, updateTime).getTaskId();
    }

    public void setScoreboard(Player player) {
        SternalBoardHandler board = new SternalBoardHandler(player);
        FileConfiguration config = core.getConfig();
        boards.put(player.getUniqueId(), board);

        if (core.isAnimationEnabled() && config.getInt("settings.scoreboard.update") != 0) return;

        String mode = config.getString("settings.mode", "DEFAULT").toLowerCase();

        if (!mode.equalsIgnoreCase("WORLD")) {
            board.updateTitle(
                    TextUtils.processPlaceholders(player, config.getString("settings.scoreboard.title")
            ));
            return;
        }

        if (!config.contains("scoreboard-world." + player.getWorld().getName())) {
            board.updateTitle(
                    TextUtils.processPlaceholders(player, config.getString("settings.scoreboard.title")
            ));
            return;
        }

        board.updateTitle(TextUtils.processPlaceholders(
                player,
                config.getString("scoreboard-world." + player.getWorld().getName() + ".title")
        ));
    }

    public void removeScoreboard(Player player) {
        SternalBoardHandler board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public void reload() {
        if (updateTask != null) {
            Bukkit.getServer().getScheduler().cancelTask(updateTask);
        }

        // TODO: 30/11/2022 view this condition, is it necessary?
        if (core.isAnimationEnabled() && updateTask != null) {
            for (SternalBoardHandler board : this.boards.values()) {
                board.updateLines("");
            }
        }
        init();
    }

    public void toggle(Player player) {
        SternalBoardHandler oldBoard = boards.remove(player.getUniqueId());
        if (oldBoard != null) {
            oldBoard.delete();
        } else {
            setScoreboard(player);
        }
    }
}
