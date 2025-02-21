package com.xism4.sternalboard.scoreboard.handler;

import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ScoreboardHandler {
    protected String intervalUpdateKey = "settings.scoreboard-interval-update";
    protected String scoreboardSectionKey = "settings.scoreboard";

    protected BukkitConfiguration configuration;
    protected SternalBoardPlugin plugin;
    protected ScoreboardManager manager;
    protected Integer updateTask;

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
        this.updateTask = this.plugin.getServer()
                .getScheduler()
                .runTaskTimerAsynchronously(
                        plugin,
                        this.task(),
                        0,
                        config.getInt(this.intervalUpdateKey, 20)
                )
                .getTaskId();
    }

    public abstract Runnable task();

    public void stop() {
        if (this.updateTask != null) {
            this.plugin.getServer().getScheduler().cancelTask(this.updateTask);
        }
    }

}
