package com.xism4.sternalboard.tabcomplete;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class PaperTabCompleter implements Listener {
    private final Completion reloadcompletion;
    private final Completion togglecompletion;

    public PaperTabCompleter() {
        reloadcompletion = Completion.completion(
                "reload", Component.text("Reload the plugin"));
        togglecompletion = Completion.completion(
                "toggle", Component.text("Toggle your actual scoreboard"));
    }

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        final String buffer = event.getBuffer();
        final String input = buffer.charAt(0) == '/' ? buffer.substring(1) : buffer;
        final String[] tokens = input.split(" ");

        if(tokens.length == 0) return;

        if (tokens.length == 1 && ("sternalboard".equalsIgnoreCase(tokens[0]) || "sb".equalsIgnoreCase(tokens[0]))) {
            final List<Completion> completions = new ArrayList<>(2);
            if (event.getSender().hasPermission("sternalboard.reload")) {
                completions.add(reloadcompletion);
            }
            if (event.getSender().hasPermission("sternalboard.toggle")) {
                completions.add(togglecompletion);
            }
            event.completions(completions);
        }
    }
}
