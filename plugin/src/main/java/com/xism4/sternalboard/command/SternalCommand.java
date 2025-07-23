package com.xism4.sternalboard.command;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.TabManager;
import com.xism4.sternalboard.util.TextUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

@Command(value = "sternalboard", alias = "sb")
public class SternalCommand extends BaseCommand {

    @Inject
    private SternalBoardPlugin plugin;
    @Inject
    private BukkitConfiguration config;
    @Inject
    @Named("animate")
    private BukkitConfiguration animConfig;
    @Inject
    private ScoreboardManager scoreboardManager;
    @Inject
    private TabManager tabManager;

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
        this.scoreboardManager.toggle(player);
    }

    @SubCommand("reload")
    @Permission("sternalboard.reload")
    public void reloadSubcommand(CommandSender sender) {
        this.config.reload();
        this.animConfig.reload();
        this.scoreboardManager.reload();
        this.tabManager.reload();

        sender.sendMessage(TextUtils.colorize("&aThe plugin has been reloaded successfully"));
    }
}
