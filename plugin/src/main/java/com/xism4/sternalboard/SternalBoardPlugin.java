package com.xism4.sternalboard;

import com.xism4.sternalboard.module.PluginModule;
import com.xism4.sternalboard.service.Service;
import com.xism4.sternalboard.misc.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.tinylog.Logger;
import revxrsal.zapper.ZapperJavaPlugin;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Injector;

import java.util.Set;

public class SternalBoardPlugin extends ZapperJavaPlugin {

    private static final int STERNAL_ID_METRICS = 13409;

    @Inject
    public Set<Service> services;

    @Override
    public void onLoad() {
        Injector.create(new PluginModule(this)).injectMembers(this);
    }

    @Override
    public void onEnable() {
        Logger.info("Starting SternalBoard Plugin");
        this.services.forEach(Service::start);
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> new Metrics(this, STERNAL_ID_METRICS));
    }

    @Override
    public void onDisable() {
        this.services.forEach(Service::stop);
    }

    public boolean placeholderCheck() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}
