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
                    p.sendMessage(plugin.getName() + ChatColor.WHITE + " The version of the plugin is " + ChatColor.GREEN + plugin.getVersion());
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (p.hasPermission("sternalboard.help")) ;
                    p.sendMessage(ChatColor.YELLOW + "----------------------------------------------------------------");
                    p.sendMessage(plugin.nombre + ChatColor.WHITE + " Whats SternalBoard? - What advantages does it have?");
                    p.sendMessage(plugin.nombre + ChatColor.WHITE + " It's a simple scoreboard plugin with the fastest Timings!");
                    p.sendMessage(plugin.nombre + ChatColor.WHITE + " Actually it's free, and it doesnt have any dependency");
                    p.sendMessage(plugin.nombre + ChatColor.GOLD + "  Also, supports" + ChatColor.RED + " 1.7 x 1.17");
                    p.sendMessage(plugin.nombre + ChatColor.GREEN + " Issues?, contact me" + ChatColor.RED + " xIsm4#9127");
                    p.sendMessage(plugin.nombre + ChatColor.BLUE + "  Don't forget rate 5 stars to the plugin!");
                    sender.sendMessage(ChatColor.YELLOW + " https://www.spigotmc.org/resources/sternalboard-optimized-async-scoreboard-hex-support-1-7-1-17.89245/");
                    p.sendMessage(ChatColor.YELLOW + "----------------------------------------------------------------");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("reload")){

                    if (p.hasPermission("sternalboard.use")) {
                        plugin.reloadConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bThe Plugin &2[&6&lSternal&e&lBoard&2] &ahas been reloaded!"));
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + " [X] U don't have the permission (sternalboard.use) to preform this action");
                    }
                    return true;
                }

                else{
                    p.sendMessage(plugin.nombre+ChatColor.RED+" The command doesn't exist!");
                    return true;
                }

            } else {
                p.sendMessage(plugin.nombre+ChatColor.translateAlternateColorCodes('&'," &fUse &e/sternalboard help &fto see more info about the plugin"));
                return true;
            }
        }

    }
}
