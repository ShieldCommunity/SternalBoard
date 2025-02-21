package com.xism4.sternalboard.command.module;

import com.xism4.sternalboard.command.SternalCommand;
import dev.triumphteam.cmd.core.BaseCommand;
import team.unnamed.inject.AbstractModule;

public class CommandModule extends AbstractModule {


    @Override
    protected void configure() {
        super.multibind(BaseCommand.class)
                .asSet()
                .to(SternalCommand.class)
                .singleton();

    }

}
