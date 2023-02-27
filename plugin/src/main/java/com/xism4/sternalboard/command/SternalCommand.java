package com.xism4.sternalboard.command;

import com.xism4.sternalboard.utils.TextUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Description;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "sternal", alias = {"sternalboard", "sternalb", "sb"})
@Description("Main command for SternalBoard")
public class SternalCommand extends BaseCommand {

    // Commands of plugin
    // /sternalboard - General
    // /sternalboard reload - Reload config
    // /sternalboard help - Help command equals of general
    // /sternalboard toggle - Toggle scoreboard

    @Default
    public void onDefault(CommandSender sender) {
        helpSubcommand(sender);
    }

    @SubCommand("help")
    public void onHelp(CommandSender sender) {
        helpSubcommand(sender);
    }

    @SubCommand("reload")
    public void onReload(CommandSender sender) {
        //TODO: Reload config
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



}
