package com.xIsm4.plugins.listeners;

import com.xIsm4.plugins.Main;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;

import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Main core;

    public PlayerListener(Main core) {
        this.core = core;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
            SternalBoard board = new SternalBoard(player);
            if (core.getConfig().getInt("settings.scoreboard.update") > 0) {
                core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> board.updateTitle(PlaceholderUtils.sanitizeString(player, core.getConfig().getString("settings.scoreboard.title"))), 0, core.getConfig().getInt("settings.scoreboard.update", 20));
                }
                core.getScoreboardManager().getBoards().put(player.getUniqueId(), board);
        }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        SternalBoard board = core.getScoreboardManager().getBoards().remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }
}
