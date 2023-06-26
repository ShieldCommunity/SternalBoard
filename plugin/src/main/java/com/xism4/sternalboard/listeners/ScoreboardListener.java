package com.xism4.sternalboard.listeners;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScoreboardListener implements Listener {

    private final SternalBoardPlugin plugin;

    public ScoreboardListener(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        plugin.getScoreboardManager().checkWorldManager(e.getPlayer());
    }


    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        plugin.getScoreboardManager().removeScoreboard(
                e.getPlayer()
        );
    }
}
