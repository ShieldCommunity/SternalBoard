package com.xism4.sternalboard.managers.tab;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.list.LegacyTabExecutor;
import com.xism4.sternalboard.managers.tab.list.ModernTabExecutor;
import com.xism4.sternalboard.utils.GameVersion;
import org.bukkit.entity.Player;

public abstract class TabExecutor {

    private static TabExecutor instance = null;

    private static TabExecutor getInstance(SternalBoardPlugin plugin) {
        if (instance == null) {
            if (GameVersion.isGreaterEqualThan(GameVersion.v1_13_R1)) {
                instance = new ModernTabExecutor(
                        plugin
                );
            } else {
                instance = new LegacyTabExecutor(
                        plugin
                );
            }
        }
        return instance;
    }

    public abstract void sendTab(Player player, String header, String footer);

    public static void sendTabList(SternalBoardPlugin plugin, Player player, String header, String footer) {
        getInstance(plugin).sendTab(
                player, header, footer
        );
    }

    public String check(String line) {
        return (line == null || line.isEmpty()) ? " " : line;
    }
}
