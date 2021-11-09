
package com.xIsm4.plugins.commands;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.managers.animation.AnimationManager;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MainCMD implements CommandExecutor {

    private Structure core;
    public MainCMD(Structure plugin) {
        this.core = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command commands, String label, String[] args) {
        if (!(sender instanceof Player)) {
            core.getLogger().warning("U are not a player!");
            return false;
        }
        Player player = (Player) sender;
        if (!(args.length > 0)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fUse &e/sternalboard help &fto see more info about the plugin"));
            return true;
        }
        
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!player.hasPermission("sternalboard.toggle")) {
                    player.sendMessage(PlaceholderUtils.colorize("&cU don't have permissions to use this command"));
                    return false;
                }
                SternalBoard b = core.getScoreboardManager().getBoards().remove(player.getUniqueId());
                if (b != null) {
                    b.delete();
                }else{
                    SternalBoard board = new SternalBoard(player);
                    core.getScoreboardManager().getBoards().put(player.getUniqueId(), board);
                    if (!core.isAnimationEnabled() && core.getConfig().getInt("settings.scoreboard.update") == 0) {
                        board.updateTitle(PlaceholderUtils.sanitizeString(player, core.getConfig().getString("settings.scoreboard.title")));
                    }
                }

        } else if (args[0].equalsIgnoreCase("help")) {
            if (!(player.hasPermission("sternalboard.help"))) {
                player.sendMessage(PlaceholderUtils.colorize("&cU don't have permissions to use this command"));
                return true;
            }

            player.sendMessage(PlaceholderUtils.colorize("&9This server is runing &b&lSternal&f&lBoard &9by xIsm4"));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!(player.hasPermission("sternalboard.reload"))) {
                player.sendMessage(PlaceholderUtils.colorize("&cU don't have permissions to use this command"));
                return true;
            }

            core.reloadConfig();
                core.setAnimateScoreboard(core.getConfig().getBoolean("settings.animated"));
                core.getScoreboardManager().reload();
            if (core.isAnimationEnabled()){
                if (core.getAnimationManager() != null){
                    core.loadAnimConfig();
                    core.getAnimationManager().reload();
                }else {
                    core.loadAnimConfig();
                    core.setAnimationManager(new AnimationManager());
                }     
            }else {
                if (core.getAnimationManager() != null){
                    core.getAnimationManager().reload();
                }
            }

            player.sendMessage(PlaceholderUtils.colorize("&aThe plugin has been reloaded sucesfully"));
            return true;

        } else {
            player.sendMessage(PlaceholderUtils.colorize("&cThe command doesn't exist!"));
        }
        return true;
    }
}
