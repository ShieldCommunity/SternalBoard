package com.xism4.sternalboard.managers.animation;

import com.google.common.collect.Lists;
import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.ScoreboardManager;
import com.xism4.sternalboard.managers.animation.tasks.LineUpdateTask;
import com.xism4.sternalboard.managers.animation.tasks.TitleUpdateTask;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private final SternalBoardPlugin plugin;
    private String title;
    private String[] lines;
    private List<Integer> taskIds;

    public AnimationManager(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        FileConfiguration config = plugin.getAnimConfig();

        if (!plugin.isAnimationEnabled()) {
            this.lines = null;
            return;
        }

        this.taskIds = new ArrayList<>();

        List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
        titleLines.replaceAll(TextUtils::colorize);
        this.title = titleLines.get(0);

        TitleUpdateTask titleUpdateTask = new TitleUpdateTask(plugin, this, titleLines);
        titleUpdateTask.runTaskTimerAsynchronously(
                plugin,
                config.getInt(
                        "scoreboard-animated.title.update-rate"),
                config.getInt(
                        "scoreboard-animated.title.update-rate")
        );
        taskIds.add(titleUpdateTask.getTaskId()
        );

        List<String> linesList = Lists.newArrayList();
        ConfigurationSection configSection = config.getConfigurationSection(
                "scoreboard-animated.score-lines"
        );

        updateLines(configSection, linesList);

        this.lines = linesList.toArray(new String[0]);
    }

    public void reload() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        FileConfiguration config = plugin.getAnimConfig();

        for (Integer taskId : taskIds) {
            scheduler.cancelTask(taskId);
        }

        this.taskIds = new ArrayList<>();

        if (!plugin.isAnimationEnabled()) {
            return;
        }

        List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");

        titleLines.replaceAll(TextUtils::colorize);

        this.title = titleLines.get(0);

        TitleUpdateTask titleUpdateTask = new TitleUpdateTask(plugin, this, titleLines);
        titleUpdateTask.runTaskTimerAsynchronously(
                plugin,
                config.getInt(
                        "scoreboard-animated.title.update-rate"),
                config.getInt(
                        "scoreboard-animated.title.update-rate")
        );
        taskIds.add(titleUpdateTask.getTaskId()
        );

        List<String> linesList = Lists.newArrayList();
        ConfigurationSection configSection = config.getConfigurationSection(
                "scoreboard-animated.score-lines"
        );

        int newLinesLength = configSection.getKeys(false).size();

        if (newLinesLength < lines.length) {
            ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
            int linesToDelete = lines.length - newLinesLength;

            for (int i = 1; i <= linesToDelete; i++) {
                int lineToDelete = lines.length - i;

                for (SternalBoardHandler sb : scoreboardManager.getBoardsHandler().values()) {
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

            list.replaceAll(TextUtils::colorize);

            linesList.add(list.get(0));

            LineUpdateTask lineUpdateTask = new LineUpdateTask(
                    plugin, this, list, lineNumber
            );
            lineUpdateTask.runTaskTimerAsynchronously(plugin, updateRate, updateRate);
            taskIds.add(lineUpdateTask.getTaskId()
            );
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
