package com.xism4.software.managers.animation.tasks;

import com.xism4.software.Structure;
import com.xism4.software.SternalBoard;import com.xism4.software.managers.ScoreboardManager;
import com.xism4.software.managers.animation.AnimationManager;
import com.xism4.software.utils.placeholders.PlaceholderUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LineUpdateTask extends BukkitRunnable {
    private final String[] lines;
    int lineNumber;
    int index;

    public LineUpdateTask(List<String> lines, int lineNumber) {
        this.lines = lines.toArray(new String[0]);
        this.lineNumber = lineNumber;
        this.index = 0;
    }

    @Override
    public void run() {
        AnimationManager animationManager = Structure.getInstance().getAnimationManager();
        animationManager.setLine(lineNumber, lines[index]);
        index++;

        if (index == lines.length) {
            index = 0;
        }

        updateLine(animationManager);
    }

    public void updateLine(AnimationManager animationManager) {
        ScoreboardManager scoreboardManager = Structure.getInstance().getScoreboardManager();

        for (SternalBoard sb : scoreboardManager.getBoards().values()) {
            String line = PlaceholderUtils.parsePAPI(sb.getPlayer(), animationManager.getLine(lineNumber));
            sb.updateLine(lineNumber, line);
        }
    }
}
