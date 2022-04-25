package com.xism4.sternalboard.managers.animation;

import com.google.common.collect.Lists;
import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.tasks.LineUpdateTask;
import com.xism4.sternalboard.managers.animation.tasks.TitleUpdateTask;
import com.xism4.sternalboard.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private String title;
    private String[] lines;
    private List<Integer> taskIds;

    public AnimationManager() {
        load();
    }

    public void load() {
        Structure core = Structure.getInstance();

        if (core.isAnimationEnabled()) {
            FileConfiguration config = core.getAnimConfig();
            this.taskIds = new ArrayList<>();

            List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
            int updateRate = config.getInt("scoreboard-animated.title.update-rate");

            titleLines.replaceAll(PlaceholderUtils::colorize);
            this.title = titleLines.get(0);

            TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
            titleUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
            taskIds.add(titleUpdateTask.getTaskId());

            List<String> linesList = Lists.newArrayList();
            ConfigurationSection configSection = config.getConfigurationSection("scoreboard-animated.score-lines");

            for (String key : configSection.getKeys(false)) {
                List<String> list = configSection.getStringList(key + ".lines");
                updateRate = configSection.getInt(key + ".update-rate");
                int lineNumber = Integer.parseInt(key);

                list.replaceAll(PlaceholderUtils::colorize);

                linesList.add(list.get(0));

                LineUpdateTask lineUpdateTask = new LineUpdateTask(list, lineNumber);
                lineUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
                taskIds.add(lineUpdateTask.getTaskId());
            }

            this.lines = linesList.toArray(new String[0]);
        } else {
            this.lines = null;
        }
    }

    public void reload() {
        Structure core = Structure.getInstance();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        FileConfiguration config = core.getAnimConfig();

        for (Integer taskId : taskIds) {
            scheduler.cancelTask(taskId);
        }

        this.taskIds = new ArrayList<>();

        if (core.isAnimationEnabled()) {
            List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
            int updateRate = config.getInt("scoreboard-animated.title.update-rate");

            for (int i = 0; i < titleLines.size(); i++) {
                titleLines.set(i, PlaceholderUtils.colorize(titleLines.get(i)));
            }

            this.title = titleLines.get(0);

            TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
            titleUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
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

            for (String key : configSection.getKeys(false)) {
                List<String> list = configSection.getStringList(key + ".lines");
                updateRate = configSection.getInt(key + ".update-rate");
                int lineNumber = Integer.parseInt(key);

                for (int i = 0; i < list.size(); i++) {
                    list.set(i, PlaceholderUtils.colorize(list.get(i)));
                }

                linesList.add(list.get(0));

                LineUpdateTask lineUpdateTask = new LineUpdateTask(list, lineNumber);
                lineUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
                taskIds.add(lineUpdateTask.getTaskId());
            }

            this.lines = linesList.toArray(new String[0]);
        }
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
