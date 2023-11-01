package com.xism4.sternalboard.managers;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.TabExecutor;
import com.xism4.sternalboard.utils.ManagerExtension;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class TablistManager extends ManagerExtension {
    private boolean enabled;
    private String header;
    private String footer;
    private int taskId = 0;

    public TablistManager(SternalBoardPlugin plugin) {
        super(plugin);

    }

    public void load() {
        FileConfiguration configuration = getConfig();

        enabled = configuration.getBoolean("tab-list.enabled", false);

        header = generate(
            configuration.getStringList("tab-list.header")
        );

        footer = generate(
            configuration.getStringList("tab-list.footer")
        );

        int interval = configuration.getInt("tab-list.update-interval", 20);

        cancel();

        if (enabled) {
            taskId = getScheduler().runTaskTimerAsynchronously(
                    plugin,
                    () -> getServer().getOnlinePlayers().forEach(this::sendTab),
                    0,
                    interval <= 0 ? 20 : interval
            ).getTaskId();
        }
    }

    public String generate(List<String> list) {
        if (list.isEmpty()) {
            return " ";
        }
        return String.join("\n", list);
    }

    public void sendTab(Player player) {
        TabExecutor.sendTabList(
                plugin,
                player,
                header,
                footer
        );
    }

    public void cancel() {
        if (isRunning()) {
            getScheduler().cancelTask(taskId);
            taskId = 0;
        }
    }

    public boolean isRunning() {
        return taskId != 0;
    }

    public String getFooter() {
        return footer;
    }

    public String getHeader() {
        return header;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
