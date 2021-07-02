package com.xIsm4.plugins;

import com.xIsm4.plugins.listeners.PlayerListener;
import com.xIsm4.plugins.managers.ScoreboardManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
 * Thanks to MrMicky.
 * ---------------
 * @author xIsm4
 * @version 1.3.5
 * ---------------
 */

@Getter
public class Main extends JavaPlugin {
    private ScoreboardManager scoreboardManager;
    private String latestversion;
    private final String version = getVersion();
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
            updateChecker();
        //AsynchronousCloseException
        this.PlaceHolderApiEXC();
        this.saveConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&6&lSternal&e&lBoard&2] &bby &9xIsm4"));
        instance = this;
        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    }

    private void PlaceHolderApiEXC() {
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null || !this.getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&b&lSternal&f&lBoard &b- &9Debug mode&7]"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou must download &bPlaceHolderApi"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD+"Link:"+ChatColor.GREEN+" https://spigotmc.org/resources/6245/");
            this.setEnabled(true);
        }
    }

    public String getLatestVersion() {
        return this.latestversion;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.reloadConfig();
            System.out.println("[SternalBoard] Reloaded Configuration");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                Player p = (Player) sender;

                if (p.hasPermission("sternalboard.use")) {
                    this.reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bThe Plugin &2[&6&lSternal&e&lBoard&2] &ahas been reloaded!"));
                } else {
                    p.sendMessage(ChatColor.DARK_RED + " [X] U don't have the permission (sternalboard.use) to preform this action");
                }
            }

        }else {
            sender.sendMessage(ChatColor.RED + " Not enougth arguments");
            return true;
        }

        return true;
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

