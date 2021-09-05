package com.xIsm4.plugins;

import com.xIsm4.plugins.commands.MainCMD;
import com.xIsm4.plugins.commands.ToggleCMD;

import com.xIsm4.plugins.listeners.PlayerListener;
import com.xIsm4.plugins.managers.ScoreboardManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;


@Getter
public class Structure extends JavaPlugin {

    private ScoreboardManager scoreboardManager;
    private static Structure instance;

    public static Structure getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        commandHandler();
        this.saveConfig();
        getLogger().info("SternalBoard has been enabled");
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        instance = this;
        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling [SternalBoard]");
        instance = null;
        scoreboardManager = null;
    }

    //Commands
    public void commandHandler() {
        this.getCommand("sternalboard").setExecutor(new MainCMD(this));
        this.getCommand("toggle").setExecutor(new ToggleCMD(this));
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }
}
