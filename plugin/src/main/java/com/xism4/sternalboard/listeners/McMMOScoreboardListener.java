package com.xism4.sternalboard.listeners;

import com.gmail.nossr50.events.scoreboard.McMMOScoreboardMakeboardEvent;
import com.gmail.nossr50.events.scoreboard.McMMOScoreboardRevertEvent;
import com.xism4.sternalboard.SternalBoardPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public class McMMOScoreboardListener implements Listener {

    private SternalBoardPlugin plugin;

    public McMMOScoreboardListener(SternalBoardPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onTriggerBoard(McMMOScoreboardMakeboardEvent event) {
        Player player = event.getTargetPlayer();

        plugin.getScoreboardManager().removeScoreboard(player);
    }

    @EventHandler
    public void onDisableBoard(McMMOScoreboardRevertEvent event) {
        Player player = event.getTargetPlayer();

        plugin.getScoreboardManager().setScoreboard(player);
    }
}
