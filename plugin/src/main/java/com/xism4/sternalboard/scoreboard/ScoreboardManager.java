package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.api.SternalBoard;
import com.xism4.sternalboard.api.repository.ObjectCacheRepository;
import com.xism4.sternalboard.manager.Manager;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.scoreboard.handler.ScoreboardHandler;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

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
        this.handler = this.factory.create();
    }

    public ObjectCacheRepository<SternalBoard> get() {
        return this.cacheRepository;
    }
}
