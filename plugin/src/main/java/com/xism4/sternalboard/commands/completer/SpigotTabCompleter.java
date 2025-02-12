package com.xism4.sternalboard.commands.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SpigotTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                                                @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return null;

        var completions = Stream.of(
                sender.hasPermission("sternalboard.reload") ? "reload" : null,
                sender.hasPermission("sternalboard.toggle") ? "toggle" : null
                )
                .filter(Objects::nonNull)
                .toList();

        return completions.isEmpty() ? null : completions;
    }
}
