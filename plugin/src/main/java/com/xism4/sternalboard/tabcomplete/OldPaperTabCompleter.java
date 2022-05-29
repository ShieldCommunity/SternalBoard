package com.xism4.sternalboard.tabcomplete;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class OldPaperTabCompleter implements Listener {

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        final String buffer = event.getBuffer();
        final String input = buffer.charAt(0) == '/' ? buffer.substring(1) : buffer;
        final String[] tokens = input.split(" ");

        if (tokens.length == 0) return;

        if (tokens.length == 1 && ("sternalboard".equalsIgnoreCase(tokens[0]) || "sb".equalsIgnoreCase(tokens[0]))) {
            final List<String> completions = new ArrayList<>(2);
            if (event.getSender().hasPermission("sternalboard.reload")) {
                completions.add("reload");
            }
            if (event.getSender().hasPermission("sternalboard.toggle")) {
                completions.add("toggle");
            }
            event.setCompletions(completions);
        }
    }
}