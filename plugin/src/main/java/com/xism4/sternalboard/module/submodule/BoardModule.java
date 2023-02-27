package com.xism4.sternalboard.module.submodule;

import com.xism4.sternalboard.api.scoreboard.ScoreboardHandler;
import com.xism4.sternalboard.scoreboard.BoardHandlerFactory;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class BoardModule extends AbstractModule {

    @Provides
    @Singleton
    public ScoreboardHandler createBoardHandler() {
        return BoardHandlerFactory.create();
    }


}
