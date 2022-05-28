package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.SternalBoard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RemoveBoardsListener implements Listener {
    private final SternalBoard core;

    public RemoveBoardsListener(SternalBoard core) {
        this.core = core;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        core.getScoreboardManager().removeScoreboard(e.getPlayer());
    }
}
