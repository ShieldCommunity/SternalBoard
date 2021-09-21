package com.xIsm4.plugins.managers.animation;

import com.google.common.collect.Lists;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.managers.animation.tasks.LineUpdateTask;
import com.xIsm4.plugins.managers.animation.tasks.TitleUpdateTask;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class AnimationManager {
    private String title;
    private final String[] lines;

    //Mainly reads animated-board.yml and then sets up the update tasks
    public AnimationManager(){
        Structure core = Structure.getInstance();
        FileConfiguration config = core.getAnimConfig();

        if (core.isAnimationEnabled()){
            //Initializing Title line update
            List<String> titleLines = config.getStringList("scoreboard-animated.title.lines");
            int updateRate = config.getInt("scoreboard-animated.title.update-rate");

            for (int i = 0; i < titleLines.size(); i++){
                titleLines.set(i, PlaceholderUtils.colorize(titleLines.get(i)));
            }

            this.title = titleLines.get(0);

            TitleUpdateTask titleUpdateTask = new TitleUpdateTask(titleLines);
            titleUpdateTask.runTaskTimer(core, updateRate, updateRate);

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
                lineUpdateTask.runTaskTimer(core, updateRate, updateRate);
            }

            this.lines = linesList.toArray(new String[0]);
        }
        else {
            this.lines = null;
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
