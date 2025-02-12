package com.xism4.sternalboard.commands;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.TextUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@dev.triumphteam.cmd.core.annotation.Command(value = "sternalboard", alias = "sb")
public class SternalCommand extends BaseCommand {

    private final SternalBoardPlugin plugin;
    private FileConfiguration config;

    public SternalCommand(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Default
    public void mainCommand(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        sender.sendMessage(TextUtils.colorize(String.format("&e&lSternal&f&lBoard &7- &7by &6xism4 &7- &7v%s", version)));
        sender.sendMessage(TextUtils.colorize("&eUse /sternalboard help &fto see more info about the plugin"));
    }

    @SubCommand("help")
    public void helpSubcommand(CommandSender sender) {
        sender.sendMessage(TextUtils.colorize("&eSternalBoard &fcommands"));
        sender.sendMessage(TextUtils.colorize("- &e/sb help&f: Shows all available commands"));
        sender.sendMessage(TextUtils.colorize("- &e/sb toggle&f: Toggles the scoreboard on or off"));
        sender.sendMessage(TextUtils.colorize("- &e/sb reload&f: Reloads the config"));
    }

    @SubCommand("toggle")
    @Permission("sternalboard.toggle")
    public void toggleSubcommand(Player player) {
        plugin.getScoreboardManager().toggle(player);
    }

    @SubCommand("reload")
    @Permission("sternalboard.reload")
    public void reloadSubcommand(CommandSender sender) {
        plugin.getRawConfig().reload();
        this.config = plugin.getConfig();

        plugin.getTablistManager().load();
        boolean isAnimationEnabled = config.getBoolean("settings.animated");
        plugin.setAnimateScoreboard(isAnimationEnabled);
        plugin.getScoreboardManager().reload();
        plugin.getRawAnimConfig().reload();

        Optional.ofNullable(plugin.getAnimationManager())
                .ifPresentOrElse(
                        animationManager -> {
                            if (isAnimationEnabled) {
                                animationManager.reload();
                            }
                        },
                        () -> {
                            if (isAnimationEnabled) {
                                plugin.setAnimationManager(new AnimationManager(plugin));
                            }
                        }
                );

        sender.sendMessage(TextUtils.colorize("&aThe plugin has been reloaded successfully"));
    }
}
