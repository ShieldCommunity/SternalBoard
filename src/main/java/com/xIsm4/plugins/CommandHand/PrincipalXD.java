package com.xIsm4.plugins.CommandHand;

import com.xIsm4.plugins.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrincipalXD implements CommandExecutor {
    private Main plugin;

    public PrincipalXD(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String length, String[] args) {

        Player jugador = (Player) sender;

        if(!(sender instanceof Player)) {
            return true;
        } else{
            if(args.length > 0) {
                if (jugador.hasPermission("sternalboard.use")) {
                    if(args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        return true;
                    }else{
                        jugador.sendMessage(ChatColor.RED+" [X] ¡U don't have the permission to preform this action!");
                    }
                }
            }
        }
        return true;
    }

}
