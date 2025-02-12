package com.xism4.sternalboard.managers.animation;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.animation.tasks.LineUpdateTask;
import com.xism4.sternalboard.managers.animation.tasks.TitleUpdateTask;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private static final String TITLE_LINES_KEY = "scoreboard-animated.title.lines";
    private static final String TITLE_UPDATE_RATE_KEY = "scoreboard-animated.title.update-rate";
    private static final String SCORE_LINES_KEY = "scoreboard-animated.score-lines";

    private final SternalBoardPlugin plugin;
    private String title;
    private String[] lines;
    private final List<Integer> taskIds;

    public AnimationManager(SternalBoardPlugin plugin) {
        this.plugin = plugin;
        this.taskIds = new ArrayList<>();
        load();
    }

    public void load() {
        if (!plugin.isAnimationEnabled()) {
            lines = null;
            return;
        }

        resetTasks();
        var config = plugin.getAnimConfig();
        loadTitle(config);
        loadLines(config);
    }

    public void reload() {
        resetTasks();
        if (plugin.isAnimationEnabled()) {
            var config = plugin.getAnimConfig();
            loadTitle(config);
            adjustLinesIfNecessary(config);
            loadLines(config);
        }
    }

    private void resetTasks() {
        if (!taskIds.isEmpty()) {
            var scheduler = Bukkit.getServer().getScheduler();
            taskIds.forEach(scheduler::cancelTask);
        }
        taskIds.clear();
    }

    private void loadTitle(FileConfiguration config) {
        var titleLines = config.getStringList(TITLE_LINES_KEY);
        titleLines.replaceAll(TextUtils::colorize);

        if (!titleLines.isEmpty()) {
            title = titleLines.get(0);
            var titleUpdateTask = new TitleUpdateTask(plugin, this, titleLines);
            int updateRate = config.getInt(TITLE_UPDATE_RATE_KEY);
            titleUpdateTask.runTaskTimerAsynchronously(plugin, updateRate, updateRate);
            taskIds.add(titleUpdateTask.getTaskId());
        }
    }

    private void loadLines(FileConfiguration config) {
        var configSection = config.getConfigurationSection(SCORE_LINES_KEY);
        if (configSection == null) return;

        var linesList = new ArrayList<String>();
        configSection.getKeys(false).forEach(key -> processLine(configSection, key, linesList));
        lines = linesList.toArray(new String[0]);
    }

    private void processLine(ConfigurationSection configSection, String key, List<String> linesList) {
        var lineVariations = configSection.getStringList(key + ".lines");
        lineVariations.replaceAll(TextUtils::colorize);

        int updateRate = configSection.getInt(key + ".update-rate");
        int lineNumber = Integer.parseInt(key);

        if (!lineVariations.isEmpty()) {
            linesList.add(lineVariations.get(0));
            var lineUpdateTask = new LineUpdateTask(plugin, this, lineVariations, lineNumber);
            lineUpdateTask.runTaskTimerAsynchronously(plugin, updateRate, updateRate);
            taskIds.add(lineUpdateTask.getTaskId());
        }
    }

    private void adjustLinesIfNecessary(FileConfiguration config) {
        var configSection = config.getConfigurationSection(SCORE_LINES_KEY);
        if (configSection == null) return;

        var newLinesLength = configSection.getKeys(false).size();
        if (lines != null && newLinesLength < lines.length) {
            linesCheck(newLinesLength);
        }
    }

    private void linesCheck(int newLinesLength) {
        var scoreboardManager = plugin.getScoreboardManager();
        var linesToDelete = lines.length - newLinesLength;

        for (int i = 1; i <= linesToDelete; i++) {
            var lineToDelete = lines.length - i;
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
