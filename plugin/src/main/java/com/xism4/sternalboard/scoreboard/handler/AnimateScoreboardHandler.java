package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;

public class AnimateScoreboardHandler extends ScoreboardHandler {

    public AnimateScoreboardHandler(
            final SternalBoardPlugin plugin,
            final BukkitConfiguration configuration,
            final ScoreboardManager manager
    ) {
        super(plugin, configuration, manager);
    }

    @Override
    public Runnable task() {
        return null;
    }
}
