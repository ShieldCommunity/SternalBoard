package com.xism4.sternalboard.module;

import com.xism4.sternalboard.manager.Manager;
import com.xism4.sternalboard.scoreboard.ScoreboardManager;
import com.xism4.sternalboard.scoreboard.TabManager;
import team.unnamed.inject.AbstractModule;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        multibind(Manager.class).asSet()
                .to(TabManager.class)
                .to(ScoreboardManager.class)
                .singleton();
    }
}
