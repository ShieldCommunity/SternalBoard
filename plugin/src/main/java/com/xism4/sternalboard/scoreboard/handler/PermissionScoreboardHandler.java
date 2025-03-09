package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.Scoreboards;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;

public class PermissionScoreboardHandler extends ScoreboardHandler {

    private static final String PERMISSIONS_SECTION = "scoreboard-permission";

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

            final ConfigurationSection defaultSection = config.getConfigurationSection(super.scoreboardSectionKey);
            final ConfigurationSection permissionBoardSection = config.getConfigurationSection(PERMISSIONS_SECTION);
            final Set<String> boardSet = Objects.requireNonNull(permissionBoardSection).getKeys(false);

            // Set default if sections doesn't exist or its empty as "fast fail/safe fail" idk.
            if (!config.isSet(PERMISSIONS_SECTION) || boardSet.isEmpty()) {
                manager.get().forEach(board -> Scoreboards.updateFromSection(board, defaultSection));
                return;
            }

            super.manager.get().forEach(board -> boardSet.stream()
                    .filter(boardName -> hasPermissionForBoard(board.getPlayer(), permissionBoardSection, boardName))
                    .map(permissionBoardSection::getConfigurationSection)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresentOrElse(section -> Scoreboards.updateFromSection(board, section),
                            () -> Scoreboards.updateFromSection(board, defaultSection))
            );
        };
    }

    private boolean hasPermissionForBoard(Player player, ConfigurationSection permissionBoardSection, String boardName) {
        String permissionNode = permissionBoardSection.getString(boardName + ".node");
        String effectivePermissionNode = permissionNode != null ? permissionNode : "sternalboard." + boardName;
        return player.hasPermission(effectivePermissionNode);
    }
}
