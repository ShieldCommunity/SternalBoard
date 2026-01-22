package com.xism4.sternalboard.scoreboard.handler;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ScoreboardHandler {

    protected String intervalUpdateKey = "settings.scoreboard-interval-update";
    protected String scoreboardSectionKey = "settings.scoreboard";

    protected BukkitConfiguration configuration;
    protected SternalBoardPlugin plugin;
    protected ScoreboardManager manager;
    protected WrappedTask updateTask;

    public ScoreboardHandler(
            final SternalBoardPlugin plugin,
            final BukkitConfiguration configuration,
            final ScoreboardManager manager
    ) {
        this.plugin = plugin;
        this.configuration = configuration;
        this.manager = manager;
    }

    public void handle() {
        final FileConfiguration config = this.configuration.get();
        this.updateTask = this.plugin.getFoliaLib()
                .getScheduler()
                .runTimerAsync(
                        this.task(),
                        0,
                        config.getInt(this.intervalUpdateKey, 20)
                );
    }

    public abstract Runnable task();

    public void stop() {
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
    }

}
