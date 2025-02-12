package com.xism4.sternalboard.module;

import com.xism4.sternalboard.BukkitConfiguration;
import com.xism4.sternalboard.SternalBoardPlugin;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

public class PluginModule extends AbstractModule {

    private final SternalBoardPlugin plugin;

    public PluginModule(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @Provides
    @Singleton
    public BukkitCommandManager<CommandSender> provideCommandManager(final SternalBoardPlugin plugin) {
        return BukkitCommandManager.create(plugin);
    }

    @Override
    protected void configure() {
        this.bind(SternalBoardPlugin.class).toInstance(plugin);

        // this.animConfig = new BukkitConfiguration(this, "animated-board");
        // this.config = new BukkitConfiguration(this, "config");

        this.bind(BukkitConfiguration.class)
                .named("animate")
                .toInstance(BukkitConfiguration.create(plugin, "animated-board"));
        this.bind(BukkitConfiguration.class)
                .toInstance(BukkitConfiguration.create(plugin, "config"));


    }
}
