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

public class toggleX implements CommandExecutor {
    private Main plugin;

    public toggleX(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " [X] ยกU aren't a player!");
            return false;
        } else {


            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("toggle")) {
                Player p = (Player) sender;
                SternalBoard board = plugin.getScoreboardManager().getBoards().remove(p.getUniqueId());
                p.sendMessage("The scoreboard has been toggled");

                if (board != null) {
                    board.delete();
                }
            } else {
                        Player p = (Player) sender;
                        p.sendMessage("The scoreboard has been toggled");
                        SternalBoard board = new SternalBoard(p);
                        if (plugin.getConfig().getInt("settings.scoreboard.update") > -0) {
                            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> board.updateTitle(PlaceholderUtils.sanitizeString(p, plugin.getConfig().getString("settings.scoreboard.title"))), -0, plugin.getConfig().getInt("settings.scoreboard.update", 20));
                        } else {
                            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> board.updateTitle(PlaceholderUtils.sanitizeString(p, plugin.getConfig().getString("settings.scoreboard.title"))));
                        }
                        plugin.getScoreboardManager().getBoards().put(p.getUniqueId(), board);
                    }
                }

            }
        return true;
    }
}


