package com.xism4.software;

import com.google.common.base.Charsets;
import com.xism4.software.commands.SternalCMD;
import com.xism4.software.listeners.AddBoardsListener;
import com.xism4.software.listeners.RemoveBoardsListener;
import com.xism4.software.managers.ScoreboardManager;
import com.xism4.software.managers.animation.AnimationManager;
import com.xism4.software.tabcomplete.OldPaperTabCompleter;
import com.xism4.software.tabcomplete.PaperTabCompleter;
import com.xism4.software.tabcomplete.SpigotTabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SetupManager extends JavaPlugin {

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
        saveDefaultConfig();
        this.saveConfig();
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
     * Load SternalBoard manager - And calls animation manager.
     * After this, all uses of @load
     *
     * @return Null, if the scoreboard manager is not accessible.
     */

    public void loadScoreboardMgr(Structure plugin) {
        if (animateScoreboard) {
            setAnimationManager(new AnimationManager());
        }

        scoreboardManager = new ScoreboardManager(plugin);
        scoreboardManager.init();
    }

    public void commandHandler(Structure plugin) {
        this.getCommand("sternalboard").setExecutor(new SternalCMD(plugin));
    }

    public void loadTabCompletions() {
        try {
            Class.forName("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
            Class.forName("net.kyori.adventure.text.Component");
            getServer().getPluginManager().registerEvents(new PaperTabCompleter(), this);
        } catch (ClassNotFoundException e) {
            if (e.getMessage().contains("Component")) {
                getServer().getPluginManager().registerEvents(new OldPaperTabCompleter(), this);
            } else {
                this.getCommand("sternalboard").setTabCompleter(new SpigotTabCompleter());
            }
        }
    }

    public void eventHandler(Structure event) {
        getServer().getPluginManager().registerEvents(new AddBoardsListener(event), this);
        getServer().getPluginManager().registerEvents(new RemoveBoardsListener(event), this);
    }

    public void setAnimateScoreboard(boolean animateScoreboard) {
        this.animateScoreboard = animateScoreboard;
    }

    public void setAnimationManager(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }
}
