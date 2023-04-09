package com.xism4.sternalboard.managers.animation.tasks;

import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LineUpdateTask extends BukkitRunnable {
    private final String[] lines;
    private final AnimationManager animationManager;
    private final ScoreboardManager scoreboardManager;
    private final SternalBoardPlugin plugin;
    int lineNumber;
    int index;

    public LineUpdateTask(SternalBoardPlugin plugin, AnimationManager animationManager, List<String> lines, int lineNumber) {
        this.plugin = plugin;
        this.animationManager = animationManager;
        this.scoreboardManager = plugin.getScoreboardManager();
        this.lines = lines.toArray(new String[0]);
        this.lineNumber = lineNumber;
        this.index = 0;
    }

    @Override
    public void run() {
        animationManager.setLine(lineNumber, lines[index]);
        index++;

        if (index == lines.length) {
            index = 0;
        }

        for (SternalBoardHandler sb : scoreboardManager.getBoardsHandler().values()) {
            String line = TextUtils.processPlaceholders(plugin, sb.getPlayer(), animationManager.getLine(lineNumber));
            sb.updateLine(lineNumber, line);
        }
    }
}
