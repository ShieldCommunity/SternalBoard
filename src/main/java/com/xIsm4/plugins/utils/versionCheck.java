package com.xIsm4.plugins.utils;

import com.xIsm4.plugins.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class versionCheck extends JavaPlugin {
    PluginDescriptionFile xIsm4 = getDescription();
    public String version = xIsm4.getVersion();

    private Main plugin;
    private String latestversion;

    public versionCheck(Main plugin) {
        this.plugin = plugin;
    }

    public void version() {
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

    public String getLatestVersion() {
        return this.latestversion;
    }
}


