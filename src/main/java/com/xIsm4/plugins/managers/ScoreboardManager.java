package com.xIsm4.plugins.managers;

import java.nio.channels.AsynchronousFileChannel;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.xIsm4.plugins.Main;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


@Getter
@Setter
public class ScoreboardManager {

    private final Main core;
    private ConcurrentMap<UUID, SternalBoard> boards = new ConcurrentHashMap<>();
    private Object AsynchronousFileChannel;

    //Implementing Main.
    public ScoreboardManager(Main core) {
        this.core = core;
    }

    //Update tasks > 20
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

    //Updating the scoreboard
    private void updateBoard(SternalBoard board) {
        List<String> lines = core.getConfig().getStringList("settings.scoreboard.lines");
        lines.replaceAll(s -> PlaceholderUtils.sanitizeString(board.getPlayer(), s));
        board.updateLines(lines);
    }

    public void viaVersionIdentifier() {
        if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
            Bukkit.getConsoleSender().sendMessage("ViaVersion it's used by ur server, u should use SternalBoard-Fix version");
            Bukkit.getConsoleSender().sendMessage("This will avoid scoreboard cuts & prevent spam-console");
            Bukkit.getConsoleSender().sendMessage(PlaceholderUtils.colorize("&chttps://github.com/xIsm4/SternalBoard"));
        }
    }
}