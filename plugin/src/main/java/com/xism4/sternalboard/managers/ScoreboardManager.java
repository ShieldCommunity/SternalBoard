package com.xism4.sternalboard.managers;

import com.xism4.sternalboard.Scoreboards;
import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.utils.ManagerExtension;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager extends ManagerExtension {
    //Constants
    private static final String SCOREBOARD_MODE_KEY = "settings.mode";
    private static final String INTERVAL_UPDATE_KEY = "settings.scoreboard-interval-update";
    private static final String SCOREBOARD_SECTION_KEY = "settings.scoreboard";
    private static final String WORLD_BLACKLIST_KEY = "settings.world-blacklist.enabled";
    private static final String WORLD_BLACKLIST_WORLDS_KEY = "settings.world-blacklist.worlds";

    private final Map<UUID, SternalBoard> boardHandlerMap;
    private Integer updateTask;

    public ScoreboardManager(SternalBoardPlugin plugin) {
        super(plugin);
        this.boardHandlerMap = new ConcurrentHashMap<>();
    }

    public Map<UUID, SternalBoard> getBoardsHandler() {
        return boardHandlerMap;
    }

    public void init() {
        FileConfiguration config = getConfig();
        String scoreboardMode = config.getString(SCOREBOARD_MODE_KEY, "NORMAL").toUpperCase(Locale.ROOT);
        int updateTime = config.getInt(INTERVAL_UPDATE_KEY, 20);

        if (updateTime <= 0) {
            config.set(INTERVAL_UPDATE_KEY, 20);
            plugin.saveConfig();
        }

        if (plugin.isAnimationEnabled()) {
            return;
        }

        updateTask = getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            ConfigurationSection defaultSection = getConfig().getConfigurationSection(SCOREBOARD_SECTION_KEY);

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
        var handler = new SternalBoard(player);
        FileConfiguration config = getConfig();
        ConfigurationSection defaultSection = config.getConfigurationSection(SCOREBOARD_SECTION_KEY);

        if (plugin.isWorldEnabled() && plugin.isAnimationEnabled() && config.getInt("settings.scoreboard.update") != 0)
            return;

        Scoreboards.updateFromSection(plugin, handler, defaultSection);
        boardHandlerMap.put(player.getUniqueId(), handler);
    }

    public void removeScoreboard(Player player) {
        Optional.ofNullable(boardHandlerMap.remove(player.getUniqueId()))
                .ifPresent(SternalBoard::delete);
    }

    public void reload() {
        if (updateTask != null) {
            getScheduler().cancelTask(updateTask);
        }

        if (plugin.isAnimationEnabled() && updateTask != null) {
            boardHandlerMap.values().forEach(board -> board.updateLines(""));
        }
        init();
    }

    public void toggle(Player player) {
        Optional.ofNullable(boardHandlerMap.remove(player.getUniqueId()))
                .ifPresent(SternalBoard::delete);

        setScoreboard(player);
    }

    private void processWorldScoreboard(SternalBoard handler, ConfigurationSection defaultSection) {
        String worldName = handler.getPlayer().getWorld().getName();

        Optional.ofNullable(getConfig().getConfigurationSection("scoreboard-world." + worldName))
                .ifPresentOrElse(
                        worldSection -> Scoreboards.updateFromSection(plugin, handler, worldSection),
                        () -> Scoreboards.updateFromSection(plugin, handler, defaultSection)
                );
    }

    private void processPermissionScoreboard(SternalBoard handler, ConfigurationSection defaultSection) {
        Set<String> permissions = Objects.requireNonNull(getConfig().getConfigurationSection("scoreboard-permission")).getKeys(true);
        FileConfiguration config = getConfig();

        Optional<ConfigurationSection> permissionSection = permissions.stream()
                .map(permission -> config.getString("scoreboard-permission." + permission + ".node"))
                .filter(Objects::nonNull)
                .filter(node -> handler.getPlayer().hasPermission(node))
                .map(permission -> config.getConfigurationSection("scoreboard-permission." + permission))
                .findFirst();

        permissionSection.ifPresentOrElse(
                section -> Scoreboards.updateFromSection(plugin, handler, section),
                () -> Scoreboards.updateFromSection(plugin, handler, defaultSection)
        );
    }

    /**
     * Verifies if the player is in a world that is blacklisted.
     */
    public void setBoardAfterCheck(Player player) {
        ScoreboardManager manager = plugin.getScoreboardManager();

        if (!plugin.getConfig().getBoolean(WORLD_BLACKLIST_KEY)) {
            if (!manager.getBoardsHandler().containsKey(player.getUniqueId())) {
                manager.setScoreboard(player);
            }
            return;
        }

        List<String> worldBlacklist = getConfig().getStringList(WORLD_BLACKLIST_WORLDS_KEY);
        if (worldBlacklist.contains(player.getWorld().getName())) {
            manager.removeScoreboard(player);
        } else {
            manager.setScoreboard(player);
        }
    }
}
