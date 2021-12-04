package com.xIsm4.plugins;

import com.xIsm4.plugins.listeners.AddBoardsListener;
import com.xIsm4.plugins.listeners.RemoveBoardsListener;
import com.xIsm4.plugins.managers.ScoreboardManager;
import com.xIsm4.plugins.managers.animation.AnimationManager;

import org.bukkit.configuration.file.YamlConfiguration;

public class Structure extends SetupManager {
    private static Structure instance;

    public static Structure getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        //Set instance
        instance = this;

        //Load config
        loadConfig();

        //Load scoreboardManager (and Animated if applicable)
        loadScoreboardMgr(this);

        //Load listeners and commands
        getServer().getPluginManager().registerEvents(new AddBoardsListener(this), this);
        getServer().getPluginManager().registerEvents(new RemoveBoardsListener(this), this);
        commandHandler(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SternalBoard");
        instance = null;
        scoreboardManager = null;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public AnimationManager getAnimationManager() {
        return this.animationManager;
    }

    public boolean isAnimationEnabled(){
        return this.animateScoreboard;
    }

    public YamlConfiguration getAnimConfig(){
        return this.animConfig;
    }
}
