package com.xism4.sternalboard.managers;

import com.xism4.sternalboard.Scoreboards;
import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager {

    private final SternalBoardPlugin plugin;
    private final Map<UUID, SternalBoardHandler> boardHandlerMap;
    private Integer updateTask;

    public ScoreboardManager(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        this.boardHandlerMap = new ConcurrentHashMap<>();
    }

    public Map<UUID, SternalBoardHandler> getBoardsHandler() {
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
        SternalBoardHandler handler = new SternalBoardHandler(player);
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection defaultSection = config.getConfigurationSection("settings.scoreboard");

        if (plugin.isAnimationEnabled() && config.getInt("settings.scoreboard.update") != 0)
            return;

        Scoreboards.updateFromSection(plugin, handler, defaultSection);
        boardHandlerMap.put(player.getUniqueId(), handler);
    }

    public void removeScoreboard(Player player) {
        SternalBoardHandler board = boardHandlerMap.remove(player.getUniqueId());
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
            for (SternalBoardHandler board : this.boardHandlerMap.values()) {
                board.updateLines("");
            }
        }
        init();
    }

    public void toggle(Player player) {
        SternalBoardHandler oldBoard = boardHandlerMap.remove(player.getUniqueId());
        if (oldBoard != null) {
            oldBoard.delete();
        } else {
            setScoreboard(player);
        }
    }

    // STATIC BOARD FEATURES
    private void processWorldScoreboard(SternalBoardHandler handler, ConfigurationSection defaultSection) {
        String worldName = handler.getPlayer().getWorld().getName();
        ConfigurationSection worldSection = plugin.getConfig()
                .getConfigurationSection("scoreboard-world." + worldName);

        if (worldSection == null) {
            Scoreboards.updateFromSection(plugin, handler, defaultSection);
            return;
        }

        Scoreboards.updateFromSection(plugin, handler, worldSection);
    }

    private void processPermissionScoreboard(SternalBoardHandler handler, ConfigurationSection defaultSection) {
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
}
