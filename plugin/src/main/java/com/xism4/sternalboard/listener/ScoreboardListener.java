package com.xism4.sternalboard.listener;

import com.xism4.sternalboard.api.SternalBoard;
import com.xism4.sternalboard.api.repository.ObjectCacheRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.inject.Inject;

public class ScoreboardListener implements Listener {

    @Inject
    private ObjectCacheRepository<SternalBoard> cacheRepository;

    @EventHandler
    private void onRegisterBoard(final PlayerJoinEvent event) {
        this.cacheRepository.create(new SternalBoard(event.getPlayer()));
    }


    @EventHandler
    private void onRemoveBoard(PlayerQuitEvent event) {
        this.cacheRepository.delete(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        // Review logic for world
    }
}
