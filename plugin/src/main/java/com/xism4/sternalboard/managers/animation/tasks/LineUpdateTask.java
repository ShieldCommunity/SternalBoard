package com.xism4.sternalboard.managers.animation.tasks;

import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LineUpdateTask extends BukkitRunnable {
    private final String[] lines;
    private final AnimationManager animationManager;
    private final ScoreboardManager scoreboardManager;
    int lineNumber;
    int index;

    public LineUpdateTask(AnimationManager animationManager, List<String> lines, int lineNumber) {
        this.animationManager = animationManager;
        this.scoreboardManager = SternalBoard.getInstance().getScoreboardManager();
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

        for (SternalBoardHandler sb : scoreboardManager.getBoards().values()) {
            String line = PlaceholderUtils.parsePAPI(sb.getPlayer(),
                    animationManager.getLine(lineNumber)
            );
            sb.updateLine(lineNumber, line);
        }
    }
}
