package com.xism4.sternalboard.manager;

import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.util.TextUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class TabManager {

    private final SternalBoardPlugin plugin;
    private Integer updateTask;

    public TabManager(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        if (updateTask != null && updateTask != 0) {
            return;
        }

        FileConfiguration config = plugin.getConfig();
        int updateInterval = config.getInt("tab-list.update-interval", 20);

        if (updateInterval <= 0) {
            config.set("tab-list.update-interval", 20);
            plugin.saveConfig();
            updateInterval = 20;
        }

        updateTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                () -> {
                    plugin.getServer().getOnlinePlayers().forEach(this::sendTabList);
                },
                0,
                updateInterval
        ).getTaskId();
    }

    public void reload() {
        cancel();
        init();
    }

    public void sendTabList(Player player) {
        FileConfiguration configuration = plugin.getConfig();

        if (SternalBoardHandler.VersionType.V1_17.isLowerOrEqual() &&!configuration.getBoolean("tab-list.enabled", false)) return;

        List<String> headerList = configuration.getStringList("tab-list.header");
        List<String> footerList = configuration.getStringList("tab-list.footer");

        String header = TextUtils.processPlaceholders(plugin, player, generate(headerList));
        String footer = TextUtils.processPlaceholders(plugin, player, generate(footerList));

        player.setPlayerListHeaderFooter(header, footer);
    }

    private String generate(List<String> list) {
        if (list.isEmpty()) {
            return " ";
        }
        return String.join("\n", list);
    }

    private void cancel() {
        if (updateTask != null && updateTask != 0) {
            plugin.getServer().getScheduler().cancelTask(updateTask);
            updateTask = 0;
        }
    }
}
