package com.xism4.sternalboard.commands;

import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SternalCommand implements CommandExecutor {

    private final Structure core;
    private final FileConfiguration config;

    public SternalCommand(Structure plugin) {
        this.core = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command commands, String label, String[] args) {

        if (!(sender instanceof Player)) {
            if (args.length <= 0) {
                sender.sendMessage(
                        PlaceholderUtils.colorize("&eUse /sternalboard help &fto see more info about the plugin")
                );
                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {
                helpCommand(sender);
                return true;
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                core.reloadConfig();
                core.setAnimateScoreboard(config.getBoolean("settings.animated"));
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
                sender.sendMessage(PlaceholderUtils.colorize("&aThe plugin has been reloaded successfully"));
                return true;
            }
            else if (args[0].equalsIgnoreCase("toggle")) {
                sender.sendMessage(
                        PlaceholderUtils.colorize("&cYou cant use this command")
                );
            }

            sender.sendMessage(PlaceholderUtils.colorize("&cCommand not recognised!"));
            return true;
        }

        Player player = (Player) sender;
        
        if (args.length <= 0) {
            player.sendMessage(
                    PlaceholderUtils.colorize("&eUse sternalboard help &fto see more info about the plugin")
            );
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                helpCommand(sender);
                return true;
            case "toggle":
                if (!player.hasPermission("sternalboard.toggle")) {
                    player.sendMessage(
                            PlaceholderUtils.colorize("&cYou cant use this command")
                    );
                    return true;
                }
                core.getScoreboardManager().toggle(player);
                return true;
            case "reload":
                if (!(player.hasPermission("sternalboard.reload"))) {
                    player.sendMessage(PlaceholderUtils.colorize("&cYou cant use this command"));
                    return true;
                }

                core.reloadConfig();
                core.setAnimateScoreboard(core.getConfig().getBoolean("settings.animated"));
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
                player.sendMessage(PlaceholderUtils.colorize("&aThe plugin has been reloaded successfully"));
                return true;
            default:
                player.sendMessage(PlaceholderUtils.colorize("&cCommand not recognised!"));
                return true;
        }
    }

    private void helpCommand(CommandSender sender){
        sender.sendMessage(
                PlaceholderUtils.colorize("&eSternalBoard &fcommands")
        );
        sender.sendMessage(
                PlaceholderUtils.colorize("- &e/sb help&f: Shows all the commands available for you")
        );
        if (sender.hasPermission("sternalboard.toggle")) {
            sender.sendMessage(
                    PlaceholderUtils.colorize("- &e/sb toggle&f: Toggles the scoreboard on or off")
            );
        }
        if (sender.hasPermission("sternalboard.reload")){
            sender.sendMessage(
                    PlaceholderUtils.colorize("- &e/sb reload&f: Reloads the config")
            );
        }
    }
}
