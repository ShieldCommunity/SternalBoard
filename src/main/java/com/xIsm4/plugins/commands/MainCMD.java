package com.xIsm4.plugins.commands;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.managers.animation.AnimationManager;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class MainCMD implements CommandExecutor {

    private final Structure core;
    private final Logger log = Structure.getInstance().getLogger();

    public MainCMD(Structure plugin) {
        this.core = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command commands, String label, String[] args) {

        if (!(sender instanceof Player)) {
            log.warning("Only players can execute commands!");
            return false;
        }

        Player player = (Player) sender;
        if (!(args.length > 0)) {
            player.sendMessage("&eUse sternalboard help &fto see more info about the plugin");
            return true;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            if (!player.hasPermission("sternalboard.toggle")) {
                player.sendMessage(PlaceholderUtils.colorize("U don't have permissions to use this command"));
                return false;
            }
            core.getScoreboardManager().toggle(player);

        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!(player.hasPermission("sternalboard.reload"))) {
                log.info("&cU don't have permissions to use this command");
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

            player.sendMessage(PlaceholderUtils.colorize("&aThe plugin has been reloaded successfully"));
            return true;

        } else {
            player.sendMessage(PlaceholderUtils.colorize("&cCommand not recognised!"));
        }
        return true;
    }
}
