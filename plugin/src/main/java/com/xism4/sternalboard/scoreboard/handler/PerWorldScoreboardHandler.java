package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.Scoreboards;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class PerWorldScoreboardHandler extends ScoreboardHandler {

    public PerWorldScoreboardHandler(
            final SternalBoardPlugin plugin,
            final BukkitConfiguration configuration,
            final ScoreboardManager manager
    ) {
        super(plugin, configuration, manager);
    }

    @Override
    public Runnable task() {
        return () -> {
            final FileConfiguration config = configuration.get();
            final ConfigurationSection defaultSection = config.getConfigurationSection(super.scoreboardSectionKey);

            this.manager.get().forEach(handler -> {
                String worldName = handler.getPlayer().getWorld().getName();

                final ConfigurationSection worldSection = config.getConfigurationSection(
                        "scoreboard-world." + worldName
                );

                if (worldSection == null) {
                    Scoreboards.updateFromSection(handler, defaultSection);
                    return;
                }

                Scoreboards.updateFromSection(handler, worldSection);
            });
        };
    }
}
