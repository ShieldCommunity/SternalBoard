package com.xism4.sternalboard.manager.service;

import com.xism4.sternalboard.manager.Manager;
import com.xism4.sternalboard.service.Service;
import team.unnamed.inject.Inject;

import java.util.Set;

public class ManagerService implements Service {

    @Inject
    private Set<Manager> managerSet;

    @Override
    public void start() {
        managerSet.forEach(Manager::init);
    }
}
