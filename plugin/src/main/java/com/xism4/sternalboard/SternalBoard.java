package com.xism4.sternalboard;

import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SternalBoard extends SternalBoardManager {

    private static SternalBoard instance;
    private final int sternalID = 13409;

    public static SternalBoard getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        commandHandler(this);
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> new Metrics(this, sternalID));
        loadTabCompletions();
        loadConfig();
        loadScoreboardMgr(this);
        eventHandler(this);
    }

    @Override
    public void onDisable() {
        getLogger().info(
                "Disabling SternalBoard "
        );
        instance = null;
        scoreboardManager = null;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public AnimationManager getAnimationManager() {
        return this.animationManager;
    }

    public boolean isAnimationEnabled() {
        return this.animateScoreboard;
    }

    public YamlConfiguration getAnimConfig() {
        return this.animConfig;
    }
}
