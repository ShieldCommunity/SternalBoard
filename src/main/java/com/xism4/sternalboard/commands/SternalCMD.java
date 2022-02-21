package com.xism4.sternalboard.commands;

import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.logging.Logger;

public class SternalCMD implements CommandExecutor {

    private final Logger LOGGER;
    private final Structure core;

    public SternalCMD(Structure plugin) {
        this.core = plugin;
        this.LOGGER = core.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command commands, String label, String[] args) {
        if (!(sender instanceof Player)) {
            LOGGER.warning("Only players can execute commands!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length <= 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse sternalboard help &fto see more info about the plugin"));
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "toggle":
                if (!player.hasPermission("sternalboard.toggle")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cant use this command"));
                    return false;
                }
                core.getScoreboardManager().toggle(player);
                break;
            case "reload":
                if (!(player.hasPermission("sternalboard.reload"))) {
                    LOGGER.info("ccYou cant use this command");
                    return true;
                }

                core.reloadConfig();
                core.setAnimateScoreboard(core.getConfig().getBoolean("settings.animated"));
                core.setViaHook(core.getConfig().getBoolean("settings.hook"));
                core.getScoreboardManager().reload();
                if (core.isAnimationEnabled()) {
                    core.loadAnimConfig();
                    if (core.getAnimationManager() != null) {
                        core.getAnimationManager().reload();
                    } else {
                        core.setAnimationManager(new AnimationManager());
                    }

                } else {
                    if (core.getAnimationManager() != null) {
                        core.getAnimationManager().reload();
                    }
                }

                player.sendMessage("§aThe plugin has been reloaded successfully");
                break;
            default:
                player.sendMessage("§cCommand not recognised!");
        }
        return true;
    }
}
