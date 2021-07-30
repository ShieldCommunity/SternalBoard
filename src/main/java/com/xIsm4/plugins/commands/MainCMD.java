package com.xIsm4.plugins.commands;

import com.xIsm4.plugins.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MainCMD implements CommandExecutor {
    private Main plugin;

    public MainCMD(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " [X] ยกU aren't a player!");
            return false;
        } else {
            Player p = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("version")) {
                    p.sendMessage(plugin.getName() + ChatColor.WHITE + " The version of the plugin is 1.13.10");
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (p.hasPermission("sternalboard.help")) ;
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&bThis server it's runing &9&lSternal&f&lBoard"));
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                        //Removed some catching threads.
                    if (p.hasPermission("sternalboard.use")) {
                        plugin.reloadConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bThe Plugin &2[&6&lSternal&e&lBoard&2] &ahas been reloaded!"));
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + " [X] U don't have the permission (sternalboard.use) to preform this action");
                    }
                    return true;
                }else{
                            p.sendMessage(ChatColor.RED + " The command doesn't exist!");
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fUse &e/sternalboard help &fto see more info about the plugin"));
                        return true;
                    }
                }
            }
        }





//Close the main thread of commands.
