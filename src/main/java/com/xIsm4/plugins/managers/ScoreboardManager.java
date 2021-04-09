package com.xIsm4.plugins.managers;

import com.xIsm4.plugins.Main;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Setter
public class ScoreboardManager {
    private final Main core;

    private ConcurrentMap<UUID, SternalBoard> boards = new ConcurrentHashMap<>();

    public ScoreboardManager(Main core) {
        this.core = core;
    }

    public void init() {
        if (core.getConfig().getInt("settings.scoreboard.update") <= 0) {
            core.getConfig().set("settings.scoreboard.update", 20);
        }

        core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> {
            for (SternalBoard board : boards.values()) {
                updateBoard(board);
            }
        }, 0, core.getConfig().getInt("settings.scoreboard.update", 20));

    }

    private void updateBoard(SternalBoard board) {
        List<String> lines = core.getConfig().getStringList("settings.scoreboard.lines");
        lines.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', PlaceholderUtils.setPlaceholders(board.getPlayer(), s)));
        board.updateLines(lines);
    }
}
