package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.scoreboard.handler.*;
import org.bukkit.configuration.file.FileConfiguration;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;
import team.unnamed.inject.Singleton;

import java.util.Locale;

@Singleton
public class ScoreboardFactory {

    private static final String SCOREBOARD_MODE_KEY = "settings.mode";

    @Inject
    private BukkitConfiguration configuration;

    @Inject
    @Named("animate")
    private BukkitConfiguration animationConfiguration;

    @Inject
    private SternalBoardPlugin plugin;

    @Inject
    private ScoreboardManager manager;

    public ScoreboardHandler create() {
        final FileConfiguration config = configuration.get();
        final var mode = config.getString(SCOREBOARD_MODE_KEY);

        if (mode == null) {
            throw new IllegalArgumentException("Scoreboard mode is not defined in the configuration file.");
        }

        return switch (mode.toUpperCase(Locale.ROOT)) {
            case "WORLD" -> new PerWorldScoreboardHandler(this.plugin, this.configuration, this.manager);
            case "PERMISSION" -> new PermissionScoreboardHandler(this.plugin, this.configuration, this.manager);
            case "ANIMATE" -> new AnimateScoreboardHandler(this.plugin, this.animationConfiguration, this.manager);
            default -> new NaturalScoreboardHandler(this.plugin, this.configuration, this.manager);
        };
    }
}
