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

@Getter
public class Main extends JavaPlugin {
    private ScoreboardManager scoreboardManager;

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
        this.saveConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[&6&lSternal&e&lBoard&2] &bby &9xIsm4"));
        instance = this;
        scoreboardManager = new ScoreboardManager(this);
        scoreboardManager.init();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.saveConfig();
            this.reloadConfig();
            System.out.println("[SternalBoard] Reloaded Configuration");
            return true;
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                Player p = (Player) sender;

                if (p.hasPermission("sternalboard.use")) {
                    this.reloadConfig();
                    saveConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bThe Plugin &2[&6&lSternal&e&lBoard&2] &ahas been reloaded!"));
                } else {
                    p.sendMessage(ChatColor.DARK_RED + " [X] U don't have the permission (sternalboard.use) to preform this action");
                }
            }


        }else{
            sender.sendMessage(ChatColor.RED+" Not enougth arguments");
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
}
