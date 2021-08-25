package com.xIsm4.plugins.commands;

import com.xIsm4.plugins.Main;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ToggleCMD implements CommandExecutor {


    private Main plugin;
    private final Map<UUID, SternalBoard> boards = new HashMap<>();

    public ToggleCMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " [X] U aren't a player!");
            return false;
        }

        Player p = (Player) sender;
        if (args.length <= 0) {
            enableScore(p);
            return true;
        }

        disableScore(p);
        return true;
    }

    public void enableScore(Player player) {
        SternalBoard board = new SternalBoard(player);
        if (plugin.getConfig().getInt("settings.scoreboard.update") > 0) 
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> board.updateTitle(PlaceholderUtils.sanitizeString(player, plugin.getConfig().getString("settings.scoreboard.title"))), 0, plugin.getConfig().getInt("settings.scoreboard.update", 20));
        else
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> board.updateTitle(PlaceholderUtils.sanitizeString(player, plugin.getConfig().getString("settings.scoreboard.title"))));
        
        plugin.getScoreboardManager().getBoards().put(player.getUniqueId(), board);
    }

    public void disableScore(Player player) {
        SternalBoard board = plugin.getScoreboardManager().getBoards().remove(player.getUniqueId());

        if (board != null)
            board.delete();
    }
}

