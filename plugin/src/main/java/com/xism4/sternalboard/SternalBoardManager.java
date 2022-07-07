package com.xism4.sternalboard;

import com.google.common.base.Charsets;
import com.xism4.sternalboard.commands.SternalCommand;
import com.xism4.sternalboard.listeners.SternalBoardListeners;
import com.xism4.sternalboard.listeners.nLoginHook;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.commands.completer.OldPaperTabCompleter;
import com.xism4.sternalboard.commands.completer.PaperTabCompleter;
import com.xism4.sternalboard.commands.completer.SpigotTabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SternalBoardManager extends JavaPlugin {

    public ScoreboardManager scoreboardManager;
    public AnimationManager animationManager;
    public YamlConfiguration animConfig;
    public boolean animateScoreboard;

    /**
     * Load the scoreboard config, and then saves it.
     * After this, all uses of @load
     *
     * @throws IllegalStateException if already was enabled (Api)
     */

    public void loadConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        //this.saveConfig();
        setAnimateScoreboard(getConfig().getBoolean("settings.animated"));
        loadAnimConfig();
    }

    /**
     * Load animated scoreboard config
     * After this, all uses of @exception
     *
     * @throws IllegalAccessError if already it's replaced.
     */

    public void loadAnimConfig() {
        File animFile = new File(this.getDataFolder(), "animated-board.yml");

        if (!animFile.exists()) {
            saveResource("animated-board.yml", false);
        }

        if (animateScoreboard) {
            YamlConfiguration animConfig = YamlConfiguration.loadConfiguration(animFile);
            InputStream defConfigStream = this.getResource("animated-board.yml");

            if (defConfigStream != null) {
                animConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }
            this.animConfig = animConfig;
        }
    }

    /**
     * Tries to hook into nLogin auth system for scoreboard
     * @throws ClassNotFoundException if class is not recognised
     */

    public void nLoginHook() {
        if (getServer().getPluginManager().getPlugin("nLogin") != null) {
            try {
                Class.forName("com.nickuc.login.api.nLoginAPIHolder");
                getServer().getPluginManager().registerEvents(new nLoginHook(), this);
            } catch (ClassNotFoundException e) {
                getLogger().severe("You are using the old version of nLogin.");
                getLogger().severe("Please upgrade to version 10.");
            }
        }
    }

    /**
     * Load SternalBoard manager - And calls animation manager.
     * After this, all uses of @load
     *
     * @return Null, if the scoreboard manager is not accessible.
     */

    public void loadScoreboardMgr(SternalBoard plugin) {
        scoreboardManager = new ScoreboardManager(plugin);

        if (animateScoreboard) {
            setAnimationManager(new AnimationManager());
        }

        scoreboardManager.init();
    }

    public void commandHandler(SternalBoard plugin) {
        this.getCommand("sternalboard").setExecutor(new SternalCommand(plugin));
    }

    public void loadTabCompletions() {
        try {
            Class.forName("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
            getServer().getPluginManager().registerEvents(
                hasAdventure()
                    ? new PaperTabCompleter()
                    : new OldPaperTabCompleter(),
                this);
        } catch (ClassNotFoundException e) {
            this.getCommand("sternalboard").setTabCompleter(new SpigotTabCompleter());
        }
    }

    public boolean hasAdventure() {
        try {
            Class<?> clazz = Class.forName("net.kyori.adventure.text.Component");
            clazz.getMethod("text", String.class);
            return true;  
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }

    public void eventHandler(SternalBoard event) {
        getServer().getPluginManager().registerEvents(new SternalBoardListeners(event), this);
    }

    public void setAnimateScoreboard(boolean animateScoreboard) {
        this.animateScoreboard = animateScoreboard;
    }

    public void setAnimationManager(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }
}
