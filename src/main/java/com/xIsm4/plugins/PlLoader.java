package com.xIsm4.plugins;

import com.google.common.base.Charsets;
import com.xIsm4.plugins.commands.MainCMD;
import com.xIsm4.plugins.commands.ToggleCMD;
import com.xIsm4.plugins.managers.ScoreboardManager;
import com.xIsm4.plugins.managers.animation.AnimationManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlLoader extends JavaPlugin {
    ScoreboardManager scoreboardManager;
    AnimationManager animationManager;
    YamlConfiguration animConfig;
    boolean animateScoreboard;

    //Load config.yml
    public void loadConfig(){
        saveDefaultConfig();
        this.saveConfig();
        setAnimateScoreboard(getConfig().getBoolean("settings.animated"));
        loadAnimConfig();
    }

    //Load animated-board.yml
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

    //Load AnimatedScoreboard
    public void loadScoreboardMgr(Structure plugin){
        if (animateScoreboard){
            setAnimationManager(new AnimationManager());
        }

        scoreboardManager = new ScoreboardManager(plugin);
        scoreboardManager.init();
    }

    //Load commands
    public void commandHandler(Structure plugin) {
        this.getCommand("sternalboard").setExecutor(new MainCMD(plugin));
        this.getCommand("toggle").setExecutor(new ToggleCMD(plugin));
    }

    public void setAnimateScoreboard(boolean animateScoreboard){
        this.animateScoreboard = animateScoreboard;
    }

    public void setAnimationManager(AnimationManager animationManager){
        this.animationManager = animationManager;
    }
}
