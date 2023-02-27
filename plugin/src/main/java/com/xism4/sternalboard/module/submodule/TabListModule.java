package com.xism4.sternalboard.module.submodule;

import com.xism4.sternalboard.api.tablist.TabListHandler;
import com.xism4.sternalboard.tablist.TabListFactory;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class TabListModule extends AbstractModule {

    @Provides
    @Singleton
    public TabListHandler createTabListHandler() {
        return TabListFactory.create();
    }
}

