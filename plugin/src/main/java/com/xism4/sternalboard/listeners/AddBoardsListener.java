package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.Structure;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
