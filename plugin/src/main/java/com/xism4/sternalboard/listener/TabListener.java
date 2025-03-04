package com.xism4.sternalboard.listener;

import com.xism4.sternalboard.scoreboard.TabManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import team.unnamed.inject.Inject;

public class TabListener implements Listener {

    @Inject
    private TabManager tabManager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        tabManager.sendTabList(player);
    }
}
