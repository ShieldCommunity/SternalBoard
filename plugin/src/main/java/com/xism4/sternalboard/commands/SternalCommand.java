package com.xism4.sternalboard.commands;

import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.logging.Logger;

public class SternalCommand implements CommandExecutor {

    private final Logger LOGGER;
    private final Structure core;

    public SternalCommand(Structure plugin) {
        this.core = plugin;
        this.LOGGER = core.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command commands, String label, String[] args) {
        Player player = (Player) sender;
        
        if (args.length <= 0) {
            player.sendMessage(
                    PlaceholderUtils.colorize("&eUse sternalboard help &fto see more info about the plugin")
            );
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
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
}
