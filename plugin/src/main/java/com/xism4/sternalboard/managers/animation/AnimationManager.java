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
    private static final String TITLE_LINES_KEY = "scoreboard-animated.title.lines";
    private static final String TITLE_UPDATE_RATE_KEY = "scoreboard-animated.title.update-rate";
    private static final String SCORE_LINES_KEY = "scoreboard-animated.score-lines";

    private final SternalBoardPlugin plugin;
    private String title;
    private String[] lines;
    private List<Integer> taskIds;

    public AnimationManager(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        if (!plugin.isAnimationEnabled()) {
            lines = null;
            return;
        }

        resetTasks();
        FileConfiguration config = plugin.getAnimConfig();
        loadTitle(config);
        loadLines(config);
    }

    public void reload() {
        resetTasks();
        if (plugin.isAnimationEnabled()) {
            FileConfiguration config = plugin.getAnimConfig();
            loadTitle(config);
            adjustLinesIfNecessary(config);
            loadLines(config);
        }
    }

    private void resetTasks() {
        if (taskIds != null) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            taskIds.forEach(scheduler::cancelTask);
        }
        taskIds = new ArrayList<>();
    }

    private void loadTitle(FileConfiguration config) {
        List<String> titleLines = config.getStringList(TITLE_LINES_KEY);
        titleLines.replaceAll(TextUtils::colorize);

        if (!titleLines.isEmpty()) {
            title = titleLines.get(0);
            TitleUpdateTask titleUpdateTask = new TitleUpdateTask(plugin, this, titleLines);
            int updateRate = config.getInt(TITLE_UPDATE_RATE_KEY);
            titleUpdateTask.runTaskTimerAsynchronously(plugin, updateRate, updateRate);
            taskIds.add(titleUpdateTask.getTaskId());
        }
    }

    private void loadLines(FileConfiguration config) {
        ConfigurationSection configSection = config.getConfigurationSection(SCORE_LINES_KEY);
        if (configSection == null) return;

        List<String> linesList = Lists.newArrayList();
        for (String key : configSection.getKeys(false)) {
            processLine(configSection, key, linesList);
        }
        lines = linesList.toArray(new String[0]);
    }

    private void processLine(ConfigurationSection configSection, String key, List<String> linesList) {
        List<String> lineVariations = configSection.getStringList(key + ".lines");
        lineVariations.replaceAll(TextUtils::colorize);

        int updateRate = configSection.getInt(key + ".update-rate");
        int lineNumber = Integer.parseInt(key);

        if (!lineVariations.isEmpty()) {
            linesList.add(lineVariations.get(0));
            LineUpdateTask lineUpdateTask = new LineUpdateTask(plugin, this, lineVariations, lineNumber);
            lineUpdateTask.runTaskTimerAsynchronously(plugin, updateRate, updateRate);
            taskIds.add(lineUpdateTask.getTaskId());
        }
    }

    private void adjustLinesIfNecessary(FileConfiguration config) {
        ConfigurationSection configSection = config.getConfigurationSection(SCORE_LINES_KEY);
        if (configSection == null) return;

        int newLinesLength = configSection.getKeys(false).size();
        if (lines != null && newLinesLength < lines.length) {
            linesCheck(newLinesLength);
        }
    }

    private void linesCheck(int newLinesLength) {
        ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
        int linesToDelete = lines.length - newLinesLength;

        for (int i = 1; i <= linesToDelete; i++) {
            int lineToDelete = lines.length - i;
            scoreboardManager.getBoardsHandler().values().forEach(sb -> sb.removeLine(lineToDelete));
        }
    }

    public String getLine(int lineNumber) {
        return lines[lineNumber];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String line) {
        title = line;
    }

    public void setLine(int lineNumber, String line) {
        if (lines != null && lineNumber >= 0 && lineNumber < lines.length) {
            lines[lineNumber] = line;
        }
    }
}