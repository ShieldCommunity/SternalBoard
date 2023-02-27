package com.xism4.sternalboard.module;

import com.xism4.sternalboard.SternalBoardPlugin;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.AbstractModule;

public class PluginModule extends AbstractModule {

    private final SternalBoardPlugin plugin;

    public PluginModule(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(SternalBoardPlugin.class).toInstance(plugin);
        bind(Plugin.class).toInstance(plugin);


    }
}
