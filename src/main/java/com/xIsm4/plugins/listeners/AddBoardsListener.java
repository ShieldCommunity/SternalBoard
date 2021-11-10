package com.xIsm4.plugins.listeners;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;

import com.xIsm4.plugins.utils.placeholders.PlaceholderUtils;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AddBoardsListener implements Listener {

    private final Structure core;

    public AddBoardsListener(Structure core) {
        this.core = core;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        core.getScoreboardManager().setScoreboard(e.getPlayer());
    }
}
