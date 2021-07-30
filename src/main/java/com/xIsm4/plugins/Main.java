package com.xIsm4.plugins;

import com.xIsm4.plugins.commands.MainCMD;
import com.xIsm4.plugins.commands.toggleX;
import com.xIsm4.plugins.listeners.PlayerListener;
import com.xIsm4.plugins.managers.ScoreboardManager;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

//import java.nio.channels.AsynchronousCloseException;


@Getter
public class Main extends JavaPlugin {
    public String rutaConfig;
    private ScoreboardManager scoreboardManager;
    public String latestversion;
    private static Main instance;


    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        commandHandler();
        this.saveConfig();
        System.out.println("SternalBoard has been enabled");
        instance = this;
        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    }

    @Override
    public void onDisable() {
        System.out.println("Disabling [SternalBoard]");
        instance = null;
        scoreboardManager = null;
    }

    public void commandHandler() {
        this.getCommand("sternalboard").setExecutor(new MainCMD(this));
        this.getCommand("toggle").setExecutor(new toggleX(this));
    }
}


