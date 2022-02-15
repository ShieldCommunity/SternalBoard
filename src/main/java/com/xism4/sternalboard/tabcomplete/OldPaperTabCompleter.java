package com.xism4.sternalboard.tabcomplete;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class OldPaperTabCompleter implements Listener {
    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer();
        String input = buffer.charAt(0) == '/' ? buffer.substring(1) : buffer;
        String[] tokens = input.split(" ");

        if (tokens.length <= 1 && (tokens[0].equalsIgnoreCase("sternalboard") || tokens[0].equalsIgnoreCase("sb"))) {
            final List<String> completions = new ArrayList<>();
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