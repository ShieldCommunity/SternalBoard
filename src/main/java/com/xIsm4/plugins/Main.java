package com.xIsm4.plugins;

import com.xIsm4.plugins.commands.MainCMD;
import com.xIsm4.plugins.listeners.PlayerListener;
import com.xIsm4.plugins.managers.ScoreboardManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.nio.channels.AsynchronousCloseException;

/**
 * SternalBoard it's for those servers that they need a simple+optimizied scoreboard.
 * Actualy u can do PullRequests <a href="https://github.com/xIsm4/SternalBoard">GitHub</a>.
 * Thanks to MrMicky (Bringing FR) com.xIsm4.plugins.FastReflection.
 * ---------------
 * @author xIsm4
 * @version 1.3.6
 * ---------------
 */


@Getter
public class Main extends JavaPlugin {
    public String rutaConfig;
    PluginDescriptionFile xIsm4 = getDescription();
    public String version = xIsm4.getVersion();
    public String nombre = ChatColor.YELLOW + "[" + ChatColor.RED + xIsm4.getName() + ChatColor.YELLOW + "]";
    private ScoreboardManager scoreboardManager;
    public String latestversion;
    private final String ver = getVersion();
    private static Main instance;


    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        registrarComandos();
        updateChecker();
        //AsynchronousCloseException();
        this.PlaceHolderApiEXC();
        this.saveConfig();
        System.out.println("SternalBoard has been enabled");
        instance = this;
        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    }

    private void PlaceHolderApiEXC() {
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null || !this.getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&b&lSternal&f&lBoard &b- &9Debug mode&7]"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must download &bPlaceHolderApi"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Link:" + ChatColor.GREEN + " https://spigotmc.org/resources/6245/");
            this.setEnabled(true);
        }
    }

    public String getLatestVersion() {
        return this.latestversion;
    }

    public void registrarComandos() {
        this.getCommand("sternalboard").setExecutor(new MainCMD(this));
    }

    @Override
    public void onDisable() {
        System.out.println("Disabling [SternalBoard]");
        instance = null;
        scoreboardManager = null;
    }

    public void updateChecker() {
        try {
            final HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=89245").openConnection();
            final int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            this.latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (this.latestversion.length() <= 7 && !this.version.equals(this.latestversion)) {
                Bukkit.getConsoleSender().sendMessage(org.bukkit.ChatColor.RED + "There is a new version available .. " + org.bukkit.ChatColor.YELLOW + "(" + org.bukkit.ChatColor.GRAY + this.latestversion + org.bukkit.ChatColor.YELLOW + ")");
                Bukkit.getConsoleSender().sendMessage(org.bukkit.ChatColor.RED + "You can download it at: " + org.bukkit.ChatColor.WHITE + "https://www.spigotmc.org/resources/sternalboard-optimized-async-scoreboard-hex-support-1-7-1-17.89245/");
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error checking update.");
        }

    }
}


