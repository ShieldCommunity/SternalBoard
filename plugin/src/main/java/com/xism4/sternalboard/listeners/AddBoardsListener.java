package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.SternalBoard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AddBoardsListener implements Listener {
    private final SternalBoard core;

    public AddBoardsListener(SternalBoard core) {
        this.core = core;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        core.getScoreboardManager().setScoreboard(e.getPlayer());
    }
}
