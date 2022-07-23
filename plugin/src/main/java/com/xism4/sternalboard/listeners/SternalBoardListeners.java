package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.SternalBoard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SternalBoardListeners implements Listener {

    private final SternalBoard core;

    public SternalBoardListeners(SternalBoard core) {
        this.core = core;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        core.getScoreboardManager().setScoreboard(e.getPlayer());

    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        core.getScoreboardManager().removeScoreboard(e.getPlayer()
        );
    }
}
