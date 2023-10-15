package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.SternalBoardPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldScoreboardListener implements Listener {

    private final SternalBoardPlugin plugin;

    public WorldScoreboardListener(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
      plugin.getScoreboardManager().setBoardAfterCheck(e.getPlayer());
    }
}
