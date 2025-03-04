package com.xism4.sternalboard.service.module;

import com.xism4.sternalboard.command.service.CommandService;
import com.xism4.sternalboard.listener.service.ListenerService;
import com.xism4.sternalboard.manager.service.ManagerService;
import com.xism4.sternalboard.service.Service;
import team.unnamed.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        super.multibind(Service.class)
                .asSet()
                .to(ManagerService.class)
                .to(CommandService.class)
                .to(ListenerService.class)
                .singleton();
    }
}
