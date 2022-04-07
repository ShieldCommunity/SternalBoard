package com.xism4.sternalboard.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpigotTabCompleter implements TabCompleter {

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                               @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length > 1) return completions;
        if (sender.hasPermission("sternalboard.reload")) {
            completions.add("reload");
        }
        if (sender.hasPermission("sternalboard.toggle")) {
            completions.add("toggle");
        }
        return completions;
    }
}
