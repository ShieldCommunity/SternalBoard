package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.api.SternalBoardHandler;
import com.xism4.sternalboard.manager.Manager;
import com.xism4.sternalboard.util.TextUtils;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.List;

@Singleton
public class TabManager implements Manager {

    @Inject
    private SternalBoardPlugin plugin;
    private Integer updateTask;

    public void init() {
        if (updateTask != null && updateTask != 0) return;

        var config = plugin.getConfig();
        var updateInterval = config.getInt("tab-list.update-interval", 20);

        if (updateInterval <= 0) {
            config.set("tab-list.update-interval", 20);
            plugin.saveConfig();
            updateInterval = 20;
        }

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
        var config = plugin.getConfig();

        if (!config.getBoolean("tab-list.enabled", false) || !SternalBoardHandler.VersionType.V1_17.isHigherOrEqual()) {
            return;
        }

        var header = TextUtils.processPlaceholders(player, generate(config.getStringList("tab-list.header")));
        var footer = TextUtils.processPlaceholders(player, generate(config.getStringList("tab-list.footer")));

        player.setPlayerListHeaderFooter(header, footer);
    }

    private String generate(List<String> list) {
        return list.isEmpty() ? " " : String.join("\n", list);
    }

    private void cancel() {
        if (updateTask != null && updateTask != 0) {
            plugin.getServer().getScheduler().cancelTask(updateTask);
            updateTask = 0;
        }
    }
}
