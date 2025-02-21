package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.Scoreboards;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;

public class PermissionScoreboardHandler extends ScoreboardHandler {

    public PermissionScoreboardHandler(
            final SternalBoardPlugin plugin,
            final BukkitConfiguration configuration,
            final ScoreboardManager manager
    ) {
        super(plugin, configuration, manager);
    }

    @Override
    public Runnable task() {
        return () -> {
            final FileConfiguration config = this.configuration.get();
            final Set<String> permissions = Objects.requireNonNull(config.getConfigurationSection("scoreboard-permission")).getKeys(true);
            final ConfigurationSection defaultSection = config.getConfigurationSection(super.scoreboardSectionKey);

            super.manager.get().forEach((board) -> permissions.stream()
                    .map(permission -> config.getString("scoreboard-permission." + permission + ".node"))
                    .filter(Objects::nonNull)
                    .filter(node -> board.getPlayer().hasPermission(node))
                    .map(permission -> config.getConfigurationSection("scoreboard-permission." + permission))
                    .findFirst()
                    .ifPresentOrElse(section -> Scoreboards.updateFromSection(board, section),
                            () -> Scoreboards.updateFromSection(board, defaultSection)
                    ));
        };
    }
}
