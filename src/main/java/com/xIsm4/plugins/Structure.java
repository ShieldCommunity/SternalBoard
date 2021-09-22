package com.xIsm4.plugins;

import com.google.common.base.Charsets;
import com.xIsm4.plugins.commands.MainCMD;
import com.xIsm4.plugins.commands.ToggleCMD;

import com.xIsm4.plugins.listeners.PlayerListener;
import com.xIsm4.plugins.managers.ScoreboardManager;
import com.xIsm4.plugins.managers.animation.AnimationManager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;


@Getter
public class Structure extends JavaPlugin {

    private ScoreboardManager scoreboardManager;

    private AnimationManager animationManager;
    private YamlConfiguration animConfig;
    private boolean animateScore;

    private static Structure instance;

    public static Structure getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        commandHandler();
        this.saveConfig();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        instance = this;

        setAnimateScore(getConfig().getBoolean("settings.animated"));
        loadAnimConfig();
        if (animateScore){
            setAnimationManager(new AnimationManager());
        }

        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SternalBoard");
        instance = null;
        scoreboardManager = null;
    }

    //Commands
    public void commandHandler() {
        this.getCommand("sternalboard").setExecutor(new MainCMD(this));
        this.getCommand("toggle").setExecutor(new ToggleCMD(this));
    }

    //Load animated-board.yml
    public void loadAnimConfig(){
        File animFile = new File(this.getDataFolder(), "animated-board.yml");

        if (!animFile.exists()){
            saveResource("animated-board.yml", false);
        }

        if (animateScore){
            YamlConfiguration animConfig = YamlConfiguration.loadConfiguration(animFile);
            InputStream defConfigStream = this.getResource("animated-board.yml");

            if (defConfigStream != null) {
                animConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }

            this.animConfig = animConfig;
        }
    }

    public void setAnimateScore(boolean animateScore){
        this.animateScore = animateScore;
    }

    public void setAnimationManager(AnimationManager animationManager){
        this.animationManager = animationManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public AnimationManager getAnimationManager() {
        return this.animationManager;
    }

    public boolean isAnimationEnabled(){
        return this.animateScore;
    }

    public YamlConfiguration getAnimConfig(){
        return this.animConfig;
    }
}
