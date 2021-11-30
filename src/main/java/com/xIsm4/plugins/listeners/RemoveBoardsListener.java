package com.xIsm4.plugins.listeners;

import com.xIsm4.plugins.Structure;
import com.xIsm4.plugins.api.scoreboard.SternalBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RemoveBoardsListener implements Listener {

    private final Structure core;

    public RemoveBoardsListener(Structure core) {
        this.core = core;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        core.getScoreboardManager().removeScoreboard(e.getPlayer());
    }
}
