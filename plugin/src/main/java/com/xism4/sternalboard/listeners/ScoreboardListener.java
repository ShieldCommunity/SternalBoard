package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.SternalBoardPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {

    private final SternalBoardPlugin plugin;

    public ScoreboardListener(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        plugin.getScoreboardManager().setBoardAfterCheck(e.getPlayer());
        plugin.getTabManager().sendTabList(e.getPlayer());
    }


    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        plugin.getScoreboardManager().removeScoreboard(
                e.getPlayer()
        );
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        plugin.getScoreboardManager().setBoardAfterCheck(e.getPlayer());
    }
}
