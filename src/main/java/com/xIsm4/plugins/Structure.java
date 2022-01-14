package com.xIsm4.plugins;

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
        instance = this;
        commandHandler(this);
        loadConfig();
        loadScoreboardMgr(this);
        eventHandler(this);
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
    public boolean isViaHookEnabled(){
        return this.viaHook;
    }

    public YamlConfiguration getAnimConfig(){
        return this.animConfig;
    }
}
