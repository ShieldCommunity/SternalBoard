package com.xism4.sternalboard.managers;

import com.xism4.sternalboard.Scoreboards;
import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.SternalBoardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager {

    private final SternalBoardPlugin plugin;
    private final Map<UUID, SternalBoard> boardHandlerMap;
    private Integer updateTask;

    public ScoreboardManager(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        this.boardHandlerMap = new ConcurrentHashMap<>();
    }

    public Map<UUID, SternalBoard> getBoardsHandler() {
        return boardHandlerMap;
    }

    public void init() {
        FileConfiguration config = plugin.getConfig();
        String scoreboardMode = config.getString("settings.mode", "NORMAL")
                .toUpperCase(Locale.ROOT);
        String intervalUpdatePath = "settings.scoreboard-interval-update";
        int updateTime = config.getInt(intervalUpdatePath);

        if (updateTime <= 0) {
            config.set(intervalUpdatePath, 20);
            updateTime = 20;
            plugin.saveConfig();
        }

        if (plugin.isAnimationEnabled()) {
            return;
        }

        updateTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            ConfigurationSection defaultSection = plugin.getConfig()
                    .getConfigurationSection("settings.scoreboard");

            boardHandlerMap.forEach((context, handler) -> {
                switch (scoreboardMode) {
                    case "WORLD":
                        processWorldScoreboard(handler, defaultSection);
                        break;
                    case "PERMISSION":
                        processPermissionScoreboard(handler, defaultSection);
                        break;
                    case "NORMAL":
                    default:
                        Scoreboards.updateFromSection(plugin, handler, defaultSection);
                        break;
                }
            });
        }, 0, updateTime).getTaskId();
    }

    public void setScoreboard(Player player) {
        SternalBoard handler = new SternalBoard(player);
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection defaultSection = config.getConfigurationSection("settings.scoreboard");

        if (plugin.isWorldEnabled() && plugin.isAnimationEnabled() && config.getInt("settings.scoreboard.update") != 0)
            return;

        Scoreboards.updateFromSection(plugin, handler, defaultSection);
        boardHandlerMap.put(player.getUniqueId(), handler);
    }

    public void removeScoreboard(Player player) {
        SternalBoard board = boardHandlerMap.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public void reload() {
        if (updateTask != null) {
            Bukkit.getServer().getScheduler().cancelTask(updateTask);
        }

        // TODO: 30/11/2022 view this condition, is it necessary?
        if (plugin.isAnimationEnabled() && updateTask != null) {
            for (SternalBoard board : this.boardHandlerMap.values()) {
                board.updateLines("");
            }
        }
        init();
    }

    public void toggle(Player player) {
        SternalBoard oldBoard = boardHandlerMap.remove(player.getUniqueId());
        if (oldBoard != null) {
            oldBoard.delete();
        } else {
            setScoreboard(player);
        }
    }

    // STATIC BOARD FEATURES
    private void processWorldScoreboard(SternalBoard handler, ConfigurationSection defaultSection) {
        String worldName = handler.getPlayer().getWorld().getName();

        ConfigurationSection worldSection = plugin.getConfig()
                .getConfigurationSection("scoreboard-world." + worldName);

        if (worldSection == null) {
            Scoreboards.updateFromSection(plugin, handler, defaultSection);
            return;
        }

        Scoreboards.updateFromSection(plugin, handler, worldSection);
    }

    private void processPermissionScoreboard(SternalBoard handler, ConfigurationSection defaultSection) {
        FileConfiguration configuration = plugin.getConfig();
        Set<String> permissions = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("scoreboard-permission"))
                .getKeys(true);

        String permissionNode;
        ConfigurationSection permissionSection = null;
        for (String key : permissions) {
            permissionNode = configuration.getString("scoreboard-permission." + key + ".node");
            if (permissionNode == null) continue;
            if (handler.getPlayer().hasPermission(permissionNode)) {
                permissionSection = configuration.getConfigurationSection("scoreboard-permission." + key);
                break;
            }
        }

        if (permissionSection == null) {
            Scoreboards.updateFromSection(plugin, handler, defaultSection);
            return;
        }

        Scoreboards.updateFromSection(plugin, handler, permissionSection);
    }

    /**
     * Verifies if the player is in a world that is blacklisted.
     */
    public void setBoardAfterCheck(Player player) {
        ScoreboardManager manager = plugin.getScoreboardManager();

        if (!plugin.getConfig().getBoolean("settings.world-blacklist.enabled")) {
            if (manager.getBoardsHandler().containsKey(player.getUniqueId())) {
                return;
            }

            manager.setScoreboard(player);
        }

        @NotNull List<String> worldBlacklist = plugin.getConfig().getStringList(
                "settings.world-blacklist.worlds"
        );

        if (worldBlacklist.contains(player.getWorld().getName())) {
            manager.removeScoreboard(player);
            return;
        }

        manager.setScoreboard(player);
    }
}
