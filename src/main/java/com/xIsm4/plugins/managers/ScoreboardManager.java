package com.xIsm4.plugins.managers;

import java.nio.channels.AsynchronousFileChannel;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xIsm4.plugins.Main;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreboardManager {
    private final Main core;
    private ConcurrentMap<UUID, SternalBoard> boards = new ConcurrentHashMap<>();
    private Object AsynchronousFileChannel;

    public ScoreboardManager(Main core) {
        this.core = core;
    }

    public void init() {
        if (core.getConfig().getInt("settings.scoreboard.update") <= 0) {
            core.getConfig().set("settings.scoreboard.update", 20);
        }

        core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
            AsynchronousFileChannel file = (java.nio.channels.AsynchronousFileChannel) AsynchronousFileChannel;
            for (SternalBoard board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, core.getConfig().getInt("settings.scoreboard.update", 20));

    }

    private void updateBoard(SternalBoard board) {
        List<String> lines = core.getConfig().getStringList("settings.scoreboard.lines");
        lines.replaceAll(s -> PlaceholderUtils.sanitizeString(board.getPlayer(), s));
        board.updateLines(lines);
    }

    private void AtemptBoard(SternalBoard board) {
        AtemptBoard(board);
        //Just atempting the board, to fix SpigotMC/craftbukkit NMS issues.
    }
}
