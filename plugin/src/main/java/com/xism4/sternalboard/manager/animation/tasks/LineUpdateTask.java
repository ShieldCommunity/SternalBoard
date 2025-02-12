package com.xism4.sternalboard.manager.animation.tasks;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.manager.ScoreboardManager;
import com.xism4.sternalboard.manager.animation.AnimationManager;
import com.xism4.sternalboard.util.TextUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LineUpdateTask extends BukkitRunnable {
    private final List<String> lines;
    private final AnimationManager animationManager;
    private final ScoreboardManager scoreboardManager;
    private final SternalBoardPlugin plugin;
    private final int lineNumber;
    private int index;

    public LineUpdateTask(SternalBoardPlugin plugin, AnimationManager animationManager, List<String> lines, int lineNumber) {
        this.plugin = plugin;
        this.animationManager = animationManager;
        this.scoreboardManager = plugin.getScoreboardManager();
        this.lines = lines;
        this.lineNumber = lineNumber;
        this.index = 0;
    }

    @Override
    public void run() {
        animationManager.setLine(lineNumber, lines.get(index));
        index = (index + 1) % lines.size();

        scoreboardManager.getBoardsHandler().values().forEach(sb -> {
            var line = TextUtils.processPlaceholders(plugin, sb.getPlayer(), animationManager.getLine(lineNumber));
            sb.updateLine(lineNumber, line);
        });
    }
}
