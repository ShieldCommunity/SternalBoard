package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.manager.Manager;
import com.xism4.sternalboard.misc.BukkitConfiguration;
import com.xism4.sternalboard.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.List;

@Singleton
public class TabManager implements Manager {

    @Inject
    private SternalBoardPlugin plugin;
    @Inject
    private BukkitConfiguration configuration;

    private Integer updateTask;

    private final String TAB_LIST_ENABLED = "tab-list.enabled";

    public void init() {
        if (updateTask != null && updateTask != 0) return;

        var config = configuration.get();
        var TAB_LIST_UPDATE_INTERVAL = "tab-list.update-interval";
        var updateInterval = config.getInt(TAB_LIST_UPDATE_INTERVAL, 20);

        if (updateInterval <= 0) {
            config.set(TAB_LIST_UPDATE_INTERVAL, 20);
            configuration.save();
            updateInterval = 20;
        }

        // Don't schedule task if option is disabled.
        if (!config.getBoolean(TAB_LIST_ENABLED, false)) return;

        updateTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                () -> plugin.getServer().getOnlinePlayers().forEach(this::sendTabList),
                0,
                updateInterval
        ).getTaskId();
    }

    public void reload() {
        cancel();
        init();
    }

    public void sendTabList(Player player) {
        var config = configuration.get();

        if (!config.getBoolean(TAB_LIST_ENABLED, false)) {
            return;
        }

        var header = TextUtils.processPlaceholders(player, generate(config.getStringList("tab-list.header")));
        var footer = TextUtils.processPlaceholders(player, generate(config.getStringList("tab-list.footer")));

        player.sendPlayerListHeaderAndFooter(TextUtils.asComponent(header), TextUtils.asComponent(footer));
        //player.setPlayerListHeaderFooter(header, footer);
    }

    private String generate(List<String> list) {
        return list.isEmpty() ? " " : String.join("\n", list);
    }

    private void cancel() {
        var config = configuration.get();

        if (updateTask != null && updateTask != 0) {
            plugin.getServer().getScheduler().cancelTask(updateTask);
            updateTask = 0;
        }

        if (!config.getBoolean(TAB_LIST_ENABLED, false)) {
            Bukkit.getOnlinePlayers().forEach(online -> online.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty()));
        }
    }
}
