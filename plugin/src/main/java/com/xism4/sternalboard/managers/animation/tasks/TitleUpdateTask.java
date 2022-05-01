package com.xism4.sternalboard.managers.animation.tasks;

import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.AnimationManager;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TitleUpdateTask extends BukkitRunnable {
    private final String[] lines;
    private final AnimationManager animationManager = Structure.getInstance().getAnimationManager();
    private final ScoreboardManager scoreboardManager = Structure.getInstance().getScoreboardManager();
    int index;

    public TitleUpdateTask(List<String> lines) {
        this.lines = lines.toArray(new String[0]);
        this.index = 0;
    }

    @Override
    public void run() {
        animationManager.setTitle(lines[index]);
        index++;

        if (index == lines.length) {
            index = 0;
        }

        for (SternalBoard sb : scoreboardManager.getBoards().values()) {
            String line = PlaceholderUtils.parsePAPI(sb.getPlayer(), animationManager.getTitle());
            sb.updateTitle(line);
        }
    }
}
