package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.Scoreboards;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class NaturalScoreboardHandler extends ScoreboardHandler {

    public NaturalScoreboardHandler(
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
            ConfigurationSection section = config.getConfigurationSection(super.scoreboardSectionKey);
            if (section == null) return;

            super.manager.get().forEach((board) -> Scoreboards.updateFromSection(board, section));
        };
    }
}
