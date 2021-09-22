package com.xIsm4.plugins.managers.animation;

import com.google.common.collect.Lists;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.managers.ScoreboardManager;
import com.xIsm4.plugins.managers.animation.tasks.LineUpdateTask;
import com.xIsm4.plugins.managers.animation.tasks.TitleUpdateTask;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

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

    //Mainly reads animated-board.yml and then sets up the update tasks
    public AnimationManager(){
        load();
    }

    public void load(){
        Structure core = Structure.getInstance();

        if (core.isAnimationEnabled()){
            FileConfiguration config = core.getAnimConfig();
            this.taskIds = new ArrayList<>();

            //Initializing Title line update
            List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
            int updateRate = config.getInt("scoreboard-animated.title.update-rate");

            for (int i = 0; i < titleLines.size(); i++){
                titleLines.set(i, PlaceholderUtils.colorize(titleLines.get(i)));
            }

            this.title = titleLines.get(0);

            TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
            titleUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
            taskIds.add(titleUpdateTask.getTaskId());

            //Initializing Score line update
            List<String> linesList = Lists.newArrayList();
            ConfigurationSection configSection = config.getConfigurationSection("scoreboard-animated.score-lines");

            for (String key : configSection.getKeys(false)) {
                List<String> list = configSection.getStringList(key + ".lines");
                updateRate = configSection.getInt(key + ".update-rate");
                int lineNumber = Integer.parseInt(key);

                for (int i = 0; i < list.size(); i++){
                    list.set(i, PlaceholderUtils.colorize(list.get(i)));
                }

                linesList.add(list.get(0));

                LineUpdateTask lineUpdateTask = new LineUpdateTask(list, lineNumber);
                lineUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
                taskIds.add(lineUpdateTask.getTaskId());
            }

            this.lines = linesList.toArray(new String[0]);
        }
        else {
            this.lines = null;
        }
    }

    public void reload(){
        Structure core = Structure.getInstance();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        FileConfiguration config = core.getAnimConfig();

        //Cancel all update tasks
        for (Integer taskId : taskIds){
            scheduler.cancelTask(taskId);
        }

        //Reset taskId list
        this.taskIds = new ArrayList<>();

        if (core.isAnimationEnabled()) {
            //Title update
            //Initializing Title line update tasks
            List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
            int updateRate = config.getInt("scoreboard-animated.title.update-rate");

            for (int i = 0; i < titleLines.size(); i++) {
                titleLines.set(i, PlaceholderUtils.colorize(titleLines.get(i)));
            }

            this.title = titleLines.get(0);

            TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
            titleUpdateTask.runTaskTimerAsynchronously(core, updateRate, updateRate);
            taskIds.add(titleUpdateTask.getTaskId());

            //Line update
            List<String> linesList = Lists.newArrayList();
            ConfigurationSection configSection = config.getConfigurationSection("scoreboard-animated.score-lines");

            //Removing unused lines, if any
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

            //Initializing Line update tasks
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

            //Set initial lines;
            this.lines = linesList.toArray(new String[0]);
        }
    }

    public String getLine(int lineNumber){
        return lines[lineNumber];
    }

    public String getTitle(){
        return title;
    }

    public void setLine(int lineNumber, String line){
        this.lines[lineNumber] = line;
    }

    public void setTitle(String line){
        this.title = line;
    }
}
