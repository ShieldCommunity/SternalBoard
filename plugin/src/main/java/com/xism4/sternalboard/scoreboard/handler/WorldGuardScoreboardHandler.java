package com.xism4.sternalboard.scoreboard.handler;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.Scoreboards;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class WorldGuardScoreboardHandler extends ScoreboardHandler {

    private static final String REGIONS_SECTION = "scoreboard-regions";

    public WorldGuardScoreboardHandler(
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
            final ConfigurationSection regionBoardSection = config.getConfigurationSection(REGIONS_SECTION);

            if (!config.isSet(REGIONS_SECTION) || regionBoardSection == null) {
                manager.get().forEach(board -> Scoreboards.updateFromSection(board, defaultSection));
                return;
            }

            super.manager.get().forEach(board -> {
                Player player = board.getPlayer();
                String regionName = getPlayerRegion(player);

                if (regionName != null && regionBoardSection.isConfigurationSection(regionName)) {
                    ConfigurationSection regionSection = regionBoardSection.getConfigurationSection(regionName);
                    Scoreboards.updateFromSection(board, Objects.requireNonNull(regionSection));
                } else {
                    Scoreboards.updateFromSection(board, defaultSection);
                }
            });
        };
    }

    private String getPlayerRegion(Player player) {
        RegionManager regionManager = getRegionManager(player);
        if (regionManager == null) return null;
        ApplicableRegionSet regions = getApplicableRegions(regionManager, player);
        return getFirstRegion(regions, player);
    }

    private RegionManager getRegionManager(Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        return container.get(BukkitAdapter.adapt(player.getWorld()));
    }

    private ApplicableRegionSet getApplicableRegions(RegionManager regionManager, Player player) {
        return regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation().toBlockLocation()));
    }

    private String getFirstRegion(ApplicableRegionSet regions, Player player) {
        for (ProtectedRegion region : regions) {
            return region.getId();
        }
        return null;
    }
}
