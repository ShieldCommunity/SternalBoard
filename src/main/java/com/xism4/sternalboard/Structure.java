package com.xism4.sternalboard;

import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import org.bukkit.configuration.file.YamlConfiguration;

public class Structure extends SetupManager {

    private static Structure instance;

    @Override
    public void onEnable() {
        instance = this;
        commandHandler(this);
        loadTabCompletions();
        loadConfig();
        loadScoreboardMgr(this);
        eventHandler(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SternalBoard 1.9v");
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

    public static Structure getInstance() {
        return instance;
    }
}
