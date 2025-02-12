package com.xism4.sternalboard.util;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.manager.ScoreboardManager;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class ManagerExtension {
    protected final SternalBoardPlugin plugin;

    public ManagerExtension(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    public SternalBoardPlugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getAnimConfig() {
        return plugin.getAnimConfig();
    }

    public Server getServer() {
        return plugin.getServer();
    }

    public BukkitScheduler getScheduler() {
        return getServer().getScheduler();
    }

    public PluginManager getPluginManager() {
        return getServer().getPluginManager();
    }

    public ScoreboardManager getScoreboards() {
        return plugin.getScoreboardManager();
    }

}
