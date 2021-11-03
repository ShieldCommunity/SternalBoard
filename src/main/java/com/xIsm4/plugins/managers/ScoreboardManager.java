package com.xIsm4.plugins.managers;

import java.nio.channels.AsynchronousFileChannel;
import java.util.List;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public class ScoreboardManager {

    private final Structure core;
    private ConcurrentMap<UUID, SternalBoard> boards = new ConcurrentHashMap<>();
    private Object AsynchronousFileChannel;

    private Integer[] taskIds;

    //Implementing Main.
    public ScoreboardManager(Structure core) {
        this.core = core;
    }

    //Fixing symbol-find
    public ConcurrentMap<UUID, SternalBoard> getBoards(){
        return this.boards;
    }

    //Update tasks > 20
    public void init() {
        taskIds = new Integer[2];

        if (core.getConfig().getInt("settings.scoreboard.update") <= 0) {
            core.getConfig().set("settings.scoreboard.update", 20);
        }

        if (!core.isAnimationEnabled()){
            taskIds[0] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
                AsynchronousFileChannel file = (java.nio.channels.AsynchronousFileChannel) AsynchronousFileChannel;
                for (SternalBoard board : this.boards.values()) {
                    updateBoard(board);
                }
            }, 0, core.getConfig().getInt("settings.scoreboard.update", 20))).getTaskId();

            if (core.getConfig().getInt("settings.scoreboard.update") > 0) {
                taskIds[1] = (core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
                    for (SternalBoard board : this.boards.values()) {
                        board.updateTitle(PlaceholderUtils.sanitizeString(board.getPlayer(), core.getConfig().getString("settings.scoreboard.title")));
                    }
                }, 0, core.getConfig().getInt("settings.scoreboard.update", 20))).getTaskId();
            }
        }
    }

    public void reload(){
        for (Integer taskId : taskIds){
            if (taskId != null){
                Bukkit.getServer().getScheduler().cancelTask(taskId);
            }
        }

        if (core.isAnimationEnabled() && taskIds[0] != null){
            for (SternalBoard board : this.boards.values()) {
                board.updateLines("");
            }
        }

        init();
    }

    //Updating the scoreboard
    private void updateBoard(SternalBoard board) {
        List<String> lines = core.getConfig().getStringList("settings.scoreboard.lines");
        lines.replaceAll(s -> PlaceholderUtils.sanitizeString(board.getPlayer(), s));
        board.updateLines(lines);
    }
}
