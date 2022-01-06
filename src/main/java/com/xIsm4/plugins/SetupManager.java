package com.xIsm4.plugins;

import com.google.common.base.Charsets;
import com.xIsm4.plugins.commands.MainCMD;
import com.xIsm4.plugins.listeners.AddBoardsListener;
import com.xIsm4.plugins.listeners.RemoveBoardsListener;
import com.xIsm4.plugins.managers.ScoreboardManager;
import com.xIsm4.plugins.managers.animation.AnimationManager;
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
    public boolean viaHook;

    /**
     * Load the scoreboard config, and then saves it.
     * After this, all uses of @load
     *
     * @throws IllegalStateException if already was enabled (Api)
     */

    public void loadConfig(){
        saveDefaultConfig();
        this.saveConfig();
        setAnimateScoreboard(getConfig().getBoolean("settings.animated"));
        setViaHook(getConfig().getBoolean("settings.hook"));
        loadAnimConfig();
    }

    /**
     * Load animated scoreboard config
     * After this, all uses of @exception
     *
     * @throws IllegalAccessError if already it's replaced.
     */

    public void loadAnimConfig(){
        File animFile = new File(this.getDataFolder(), "animated-board.yml");

        if (!animFile.exists()){
            saveResource("animated-board.yml", false);
        }

        if (animateScoreboard){
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

    public void loadScoreboardMgr(Structure plugin){
        if (animateScoreboard){
            setAnimationManager(new AnimationManager());
        }

        scoreboardManager = new ScoreboardManager(plugin);
        scoreboardManager.init();
    }

    public void commandHandler(Structure plugin) {
        this.getCommand("sternalboard").setExecutor(new MainCMD(plugin));
    }

    public void eventHandler(Structure event){
        getServer().getPluginManager().registerEvents(new AddBoardsListener(event), this);
        getServer().getPluginManager().registerEvents(new RemoveBoardsListener(event), this);
    }

    public void setAnimateScoreboard(boolean animateScoreboard){
        this.animateScoreboard = animateScoreboard;
    }
    public void setViaHook(boolean viaHook){
        this.viaHook = viaHook;

    }

    public void setAnimationManager(AnimationManager animationManager){
        this.animationManager = animationManager;
    }
}
