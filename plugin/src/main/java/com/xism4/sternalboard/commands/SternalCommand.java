package com.xism4.sternalboard.commands;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SternalCommand implements CommandExecutor {

    private final SternalBoardPlugin plugin;
    private FileConfiguration config;

    public SternalCommand(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command commands,
            @NotNull String label,
            String[] args
    ){
        if (args.length == 0) {
            sender.sendMessage(
                    TextUtils.colorize("&e&lSternal&f&lBoard &7- &7by &6xism4")
            );
            sender.sendMessage(
                    TextUtils.colorize("&eUse /sternalboard help &fto see more info about the plugin")
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
                            TextUtils.colorize("&cCommand not recognized!")
                    );
                    return true;
            }
        }
    }

    private void helpSubcommand(CommandSender sender){
        sender.sendMessage(
                TextUtils.colorize("&eSternalBoard &fcommands")
        );
        sender.sendMessage(
                TextUtils.colorize(
                        "- &e/sb help&f: Shows all the commands available for you")
        );
        if (sender.hasPermission("sternalboard.toggle") && sender instanceof Player) {
            sender.sendMessage(
                    TextUtils.colorize(
                            "- &e/sb toggle&f: Toggles the scoreboard on or off")
            );
        }
        if (sender.hasPermission("sternalboard.reload")){
            sender.sendMessage(
                    TextUtils.colorize(
                            "- &e/sb reload&f: Reloads the config")
            );
        }
    }

    private void toggleSubcommand(CommandSender sender) {
        if (sender instanceof Player){
            if (sender.hasPermission("sternalboard.toggle")){
                plugin.getScoreboardManager().toggle((Player) sender);
                return;
            }
        }
        noPermission(sender);
    }

    private void reloadSubcommand(CommandSender sender) {
        if (sender.hasPermission("sternalboard.reload")){
            plugin.getRawConfig().reload();
            this.config = plugin.getConfig();
            plugin.setAnimateScoreboard(config.getBoolean("settings.animated"));
            plugin.getScoreboardManager().reload();
            if (plugin.isAnimationEnabled()) {
                plugin.getRawAnimConfig().reload();
                if (plugin.getAnimationManager() != null) {
                    plugin.getAnimationManager().reload();
                } else {
                    plugin.setAnimationManager(new AnimationManager(plugin));
                }
            } else {
                if (plugin.getAnimationManager() != null) {
                    plugin.getAnimationManager().reload();
                }
            }
            sender.sendMessage(TextUtils.colorize(
                    "&aThe plugin has been reloaded successfully")
            );
            return;
        }
        noPermission(sender);
    }

    private void noPermission(CommandSender sender){
        sender.sendMessage(
                TextUtils.colorize("&cYou cant use this command")
        );
    }
}
