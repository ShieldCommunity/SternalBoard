package com.xism4.sternalboard.service;

import com.xism4.sternalboard.api.Service;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;
import java.util.Set;

public class ListenerService implements Service {

    @Inject
    private Set<Listener> listeners;

    @Inject
    private Plugin plugin;

    @Override
    public void start() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        listeners.forEach( listener -> {
            pluginManager.registerEvents(listener, plugin);
        });
    }
}
