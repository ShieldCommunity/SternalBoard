package com.xism4.sternalboard.commands.completer;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.stream.Stream;

public final class PaperTabCompleter implements Listener {

    private final Completion reloadCompletion = Completion.completion(
            "reload", Component.text("Reload the plugin", NamedTextColor.GREEN));

    private final Completion toggleCompletion = Completion.completion(
            "toggle", Component.text("Toggle your actual scoreboard", NamedTextColor.GREEN));

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        var buffer = event.getBuffer();
        var input = buffer.startsWith("/") ? buffer.substring(1) : buffer;
        var tokens = input.split(" ");

        if (tokens.length == 0) return;

        if (tokens.length == 1 && ("sternalboard".equalsIgnoreCase(tokens[0]) || "sb".equalsIgnoreCase(tokens[0]))) {
            var completions = Stream.of(
                    event.getSender().hasPermission("sternalboard.reload") ? reloadCompletion : null,
                    event.getSender().hasPermission("sternalboard.toggle") ? toggleCompletion : null
                    )
                    .filter(Objects::nonNull)
                    .toList();

            event.completions(completions);
        }
    }
}
