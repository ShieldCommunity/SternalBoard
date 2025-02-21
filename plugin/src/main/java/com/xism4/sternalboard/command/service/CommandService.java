package com.xism4.sternalboard.command.service;

import com.xism4.sternalboard.service.Service;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.BaseCommand;
import org.bukkit.command.CommandSender;
import team.unnamed.inject.Inject;

import java.util.Set;

public class CommandService implements Service {

    @Inject
    private BukkitCommandManager<CommandSender> manager;

    @Inject
    private Set<BaseCommand> commands;

    @Override
    public void start() {
        this.commands.forEach(manager::registerCommand);
    }

    @Override
    public void stop() {
        this.commands.forEach(manager::unregisterCommand);
    }
}
