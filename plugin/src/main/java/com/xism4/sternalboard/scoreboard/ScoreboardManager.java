package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.api.SternalBoard;
import com.xism4.sternalboard.api.repository.ObjectCacheRepository;
import com.xism4.sternalboard.manager.Manager;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.handler.ScoreboardHandler;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.UUID;

@Singleton
public class ScoreboardManager implements Manager {

    @Inject
    private SternalBoardPlugin plugin;
    @Inject
    private BukkitConfiguration configuration;
    @Inject
    private ScoreboardFactory factory;
    @Inject
    private ObjectCacheRepository<SternalBoard> cacheRepository;

    private ScoreboardHandler handler;

    public void init() {
        this.handler = this.factory.create();
        this.handler.handle();
    }

    public void reload() {
        this.handler.stop();
        this.init();
    }

    @Override
    public void toggle(Player player) {
        UUID uuid = player.getUniqueId();

        if (cacheRepository.exists(uuid)) {
            SternalBoard board = cacheRepository.find(uuid);
            board.delete();
            cacheRepository.delete(uuid);
            return;
        }

        SternalBoard board = new SternalBoard(player);
        cacheRepository.create(board);
    }
    public ObjectCacheRepository<SternalBoard> get() {
        return this.cacheRepository;
    }
}
