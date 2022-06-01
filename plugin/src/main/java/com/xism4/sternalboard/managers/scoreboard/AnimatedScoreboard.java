package com.xism4.sternalboard.managers.scoreboard;

import com.google.common.collect.Lists;
import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.tasks.LineUpdateTask;
import com.xism4.sternalboard.managers.animation.tasks.TitleUpdateTask;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class AnimatedScoreboard extends SimpleScoreboard {

    private final Structure core;
    private String title;
    private String[] lines;
    private List<Integer> taskIds;

    public AnimatedScoreboard(Structure core) {
        super(core);
        this.core = core;
    }

    @Override
    public ConcurrentMap<UUID, SternalBoard> getBoards() {
        return super.getBoards();
    }

    @Override
    public void run() {
        FileConfiguration config = core.getAnimConfig();

        if (!core.isAnimationEnabled()) {
            this.lines = null;
            return;
        }

        this.taskIds = new ArrayList<>();

        List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
        titleLines.replaceAll(PlaceholderUtils::colorize);
        this.title = titleLines.get(0);

        TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
        titleUpdateTask.runTaskTimerAsynchronously(
                core,
                config.getInt("scoreboard-animated.title.update-rate"),
                config.getInt("scoreboard-animated.title.update-rate")
        );
        taskIds.add(titleUpdateTask.getTaskId());

        List<String> linesList = Lists.newArrayList();
        ConfigurationSection configSection = config.getConfigurationSection("scoreboard-animated.score-lines");

        updateLines(configSection, linesList);

        this.lines = linesList.toArray(new String[0]);
    }

    @Override
    public void setScoreboard(Player player) {
        super.setScoreboard(player);
    }

    @Override
    public void removeScoreboard(Player player) {
        super.removeScoreboard(player);
    }

    @Override
    public void reloadScoreboard() {
        Structure core = Structure.getInstance();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        FileConfiguration config = core.getAnimConfig();

        for (Integer taskId : taskIds) {
            scheduler.cancelTask(taskId);
        }

        this.taskIds = new ArrayList<>();

        if (core.isAnimationEnabled()) {
            return;
        }

        List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");

        titleLines.replaceAll(PlaceholderUtils::colorize);

        this.title = titleLines.get(0);

        TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
        titleUpdateTask.runTaskTimerAsynchronously(
                core,
                config.getInt("scoreboard-animated.title.update-rate"),
                config.getInt("scoreboard-animated.title.update-rate")
        );
        taskIds.add(titleUpdateTask.getTaskId());

        List<String> linesList = Lists.newArrayList();
        ConfigurationSection configSection = config.getConfigurationSection("scoreboard-animated.score-lines");

        int newLinesLength = configSection.getKeys(false).size();

        if (newLinesLength < lines.length) {
            ScoreboardManager scoreboardManager = core.getScoreboardManager();
            int linesToDelete = lines.length - newLinesLength;

            for (int i = 1; i <= linesToDelete; i++) {
                int lineToDelete = lines.length - i;

                for (SternalBoard sb : scoreboardManager.getBoards().values()) {
                    sb.removeLine(lineToDelete);
                }
            }
        }

        updateLines(configSection, linesList);
        this.lines = linesList.toArray(new String[0]);
    }

    private void updateLines(ConfigurationSection configSection, List<String> linesList) {
        for (String key : configSection.getKeys(false)) {
            List<String> list = configSection.getStringList(key + ".lines");
            int updateRate = configSection.getInt(key + ".update-rate");
            int lineNumber = Integer.parseInt(key);

            list.replaceAll(PlaceholderUtils::colorize);

            linesList.add(list.get(0));

            LineUpdateTask lineUpdateTask = new LineUpdateTask(list, lineNumber);
            lineUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
            taskIds.add(lineUpdateTask.getTaskId());
        }
    }

    @Override
    public void updateScoreboard(SternalBoard board) {
        super.updateScoreboard(board);
    }

    @Override
    public void toggleScoreboard(Player player) {
        super.toggleScoreboard(player);
    }

    public String getLine(int lineNumber) {
        return lines[lineNumber];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String line) {
        this.title = line;
    }

    public void setLine(int lineNumber, String line) {
        this.lines[lineNumber] = line;
    }
}
