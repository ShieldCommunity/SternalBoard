package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.Scoreboards;
import com.xism4.sternalboard.manager.animation.AnimationService;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

public class AnimateScoreboardHandler extends ScoreboardHandler {

    private final AnimationService animationService;

    @Inject
    public AnimateScoreboardHandler(
            SternalBoardPlugin plugin,
            @Named("animate") BukkitConfiguration configuration,
            ScoreboardManager manager,
            AnimationService animationService
    ) {
        super(plugin, configuration, manager);
        this.animationService = animationService;
    }

    @Override
    public Runnable task() {
        return () -> {
            FileConfiguration config = configuration.get();
            ConfigurationSection animatedSection = config.getConfigurationSection("scoreboard-animated");
            ConfigurationSection frameSection = animationService.next(animatedSection);

            if (animatedSection == null && frameSection == null) return;
            manager.get().forEach(board ->
                    Scoreboards.updateFromSection(board, frameSection)
            );
        };
    }

    @Override
    public void handle() {
        animationService.reset();
        super.handle();
    }
}
