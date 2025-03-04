package com.xism4.sternalboard.module;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.api.SternalBoard;
import com.xism4.sternalboard.api.repository.ObjectCacheRepository;
import com.xism4.sternalboard.api.repository.impl.ObjectCacheRepositoryImpl;
import com.xism4.sternalboard.command.module.CommandModule;
import com.xism4.sternalboard.listener.module.ListenerModule;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.service.module.ServiceModule;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Named;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;
import team.unnamed.inject.key.TypeReference;

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

    @Provides
    @Singleton
    @Named("animate")
    public BukkitConfiguration provideAnimateConfig(final SternalBoardPlugin plugin) {
        return BukkitConfiguration.create(plugin, "animated-board");
    }

    @Provides
    @Singleton
    public BukkitConfiguration provideConfig(final SternalBoardPlugin plugin) {
        return BukkitConfiguration.create(plugin, "config");
    }

    @Override
    protected void configure() {
        this.bind(SternalBoardPlugin.class).toInstance(plugin);

        this.bind(new TypeReference<ObjectCacheRepository<SternalBoard>>() {
        }).toInstance(new ObjectCacheRepositoryImpl());

        super.install(new ManagerModule());
        super.install(new ServiceModule());
        super.install(new CommandModule());
        super.install(new ListenerModule());
    }
}
