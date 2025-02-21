package com.xism4.sternalboard.listener.service;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.service.Service;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import team.unnamed.inject.Inject;

import java.util.Set;

public class ListenerService implements Service {

    @Inject
    private SternalBoardPlugin plugin;

    @Inject
    private Set<Listener> listeners;

    @Override
    public void start() {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        this.listeners.forEach(listener -> pluginManager.registerEvents(listener, plugin));
    }
}
