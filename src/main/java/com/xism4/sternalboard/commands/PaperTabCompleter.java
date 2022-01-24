package com.xism4.sternalboard.commands;

import java.util.ArrayList;
import java.util.List;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.kyori.adventure.text.Component;

public class PaperTabCompleter implements Listener {
    private final Completion reloadcompletion;
    private final Completion togglecompletion;

    public PaperTabCompleter(){
        reloadcompletion = Completion.completion(
            "reload", Component.text("Reload the plugin"));
        togglecompletion = Completion.completion(
            "toggle", Component.text("Toggle your actual scoreboard"));
    }
    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event){
        String buffer = event.getBuffer();
        String input = buffer.charAt(0) == '/' ? buffer.substring(1) : buffer;
        String[] tokens = input.split(" ");

        if(tokens.length <= 1 && (tokens[0].equalsIgnoreCase("sternalboard") || tokens[0].equalsIgnoreCase("sb"))){
            final List<Completion> completions = new ArrayList<>();
            if(event.getSender().hasPermission("sternalboard.reload")){
                completions.add(reloadcompletion);
            }
            if(event.getSender().hasPermission("sternalboard.toggle")){
                completions.add(togglecompletion);
            }
            event.completions(completions);
        }
    }
}
