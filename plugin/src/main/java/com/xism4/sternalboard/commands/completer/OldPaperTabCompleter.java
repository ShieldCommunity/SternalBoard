package com.xism4.sternalboard.commands.completer;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.stream.Stream;

public final class OldPaperTabCompleter implements Listener {

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        var buffer = event.getBuffer();
        var sender = event.getSender();

        var input = (
                sender instanceof ConsoleCommandSender && buffer.isEmpty()) ? buffer : buffer.startsWith("/") ? buffer.substring(1) : buffer;

        var tokens = input.split(" ");

        if (tokens.length == 0) return;

        if (tokens.length == 1 && ("sternalboard".equalsIgnoreCase(tokens[0]) || "sb".equalsIgnoreCase(tokens[0]))) {
            var completions = Stream.of(
                    sender.hasPermission("sternalboard.reload") ? "reload" : null,
                    sender.hasPermission("sternalboard.toggle") ? "toggle" : null
            ).filter(Objects::nonNull).toList();

            event.setCompletions(completions);
        }
    }
}
