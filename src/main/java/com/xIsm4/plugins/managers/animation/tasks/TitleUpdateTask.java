package com.xIsm4.plugins.managers.animation.tasks;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.managers.ScoreboardManager;
import com.xIsm4.plugins.managers.animation.AnimationManager;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TitleUpdateTask extends BukkitRunnable {
    private final String[] lines;
    int index;

    public TitleUpdateTask(String[] lines){
        this.lines = lines;
        this.index = 0;
    }

    public TitleUpdateTask(List<String> lines){
        this.lines = lines.toArray(new String[0]);
        this.index = 0;
    }

    @Override
    public void run() {
        AnimationManager animationManager = Structure.getInstance().getAnimationManager();
        animationManager.setTitle(lines[index]);
        index++;

        if (index == lines.length){
            index = 0;
        }

        updateLine(animationManager);
    }

    public void updateLine(AnimationManager animationManager){
        ScoreboardManager scoreboardManager = Structure.getInstance().getScoreboardManager();

        for (SternalBoard sb : scoreboardManager.getBoards().values()){
            String line = PlaceholderUtils.parsePAPI(sb.getPlayer(), animationManager.getTitle());
            sb.updateTitle(line);
        }
    }
}
