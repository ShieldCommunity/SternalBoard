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
        worldCheck(e.getPlayer());
    }


    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        plugin.getScoreboardManager().removeScoreboard(
                e.getPlayer()
        );
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        worldCheck(e.getPlayer());
    }

    private void worldCheck(Player player) {
        ScoreboardManager manager = plugin.getScoreboardManager();

        if (!plugin.getConfig().getBoolean("settings.world-blacklist.enabled")) {
            if (manager.getBoardsHandler().containsKey(player.getUniqueId())) {
                return;
            }

            manager.setScoreboard(player);
        }

        @NotNull List<String> worldBlacklist = plugin.getConfig().getStringList("settings.world-blacklist.worlds");

        if (worldBlacklist.contains(player.getWorld().getName())) {
            manager.removeScoreboard(player);
            return;
        }

        manager.setScoreboard(player);
    }
}
