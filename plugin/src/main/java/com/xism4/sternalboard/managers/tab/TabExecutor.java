package com.xism4.sternalboard.managers.tab;

import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.list.LegacyTabExecutor;
import com.xism4.sternalboard.managers.tab.list.ModernTabExecutor;
import org.bukkit.entity.Player;

public abstract class TabExecutor {

    private static TabExecutor instance = null;

    private static TabExecutor getInstance(SternalBoardPlugin plugin) {
        if (instance == null) {
            boolean isModern = SternalBoardHandler.VersionType.V1_13.isHigherOrEqual();
            switch (isModern ? "MODERN" : "LEGACY") {
                case "MODERN":
                    instance = new ModernTabExecutor(plugin);
                    break;
                case "LEGACY":
                    instance = new LegacyTabExecutor(plugin);
                    break;
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
