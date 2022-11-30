package com.xism4.sternalboard.commands;

import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SternalCommand implements CommandExecutor {

    private final SternalBoard core;
    private FileConfiguration config;

    public SternalCommand(SternalBoard plugin) {
        this.core = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command commands, String label, String[] args){
        if (args.length == 0) {
            sender.sendMessage(
                    TextUtils.parseToLegacyColors("&eUse /sternalboard help &fto see more info about the plugin")
            );
            return true;
        }
        else {
            switch (args[0].toLowerCase()){
                case "help":
                    helpSubcommand(sender);
                    return true;
                case "toggle":
                    toggleSubcommand(sender);
                    return true;
                case "reload":
                    reloadSubcommand(sender);
                    return true;
                default:
                    sender.sendMessage(
                            TextUtils.parseToLegacyColors("&cCommand not recognized!")
                    );
                    return true;
            }
        }
    }

    private void helpSubcommand(CommandSender sender){
        sender.sendMessage(
                TextUtils.parseToLegacyColors("&eSternalBoard &fcommands")
        );
        sender.sendMessage(
                TextUtils.parseToLegacyColors(
                        "- &e/sb help&f: Shows all the commands available for you")
        );
        if (sender.hasPermission("sternalboard.toggle") && sender instanceof Player) {
            sender.sendMessage(
                    TextUtils.parseToLegacyColors(
                            "- &e/sb toggle&f: Toggles the scoreboard on or off")
            );
        }
        if (sender.hasPermission("sternalboard.reload")){
            sender.sendMessage(
                    TextUtils.parseToLegacyColors(
                            "- &e/sb reload&f: Reloads the config")
            );
        }
    }

    private void toggleSubcommand(CommandSender sender) {
        if (sender instanceof Player){
            if (sender.hasPermission("sternalboard.toggle")){
                core.getScoreboardManager().toggle((Player) sender);
                return;
            }
        }
        noPermission(sender);
    }

    private void reloadSubcommand(CommandSender sender) {
        if (sender.hasPermission("sternalboard.reload")){
            core.reloadConfig();
            this.config = core.getConfig();
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
            sender.sendMessage(TextUtils.parseToLegacyColors(
                    "&aThe plugin has been reloaded successfully")
            );
            return;
        }
        noPermission(sender);
    }

    private void noPermission(CommandSender sender){
        sender.sendMessage(
                TextUtils.parseToLegacyColors("&cYou cant use this command")
        );
    }
}
