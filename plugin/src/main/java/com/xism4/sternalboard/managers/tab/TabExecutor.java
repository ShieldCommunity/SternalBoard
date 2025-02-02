package com.xism4.sternalboard.managers.tab;

import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.list.ModernTabExecutor;
import org.bukkit.entity.Player;

public abstract class TabExecutor {

    private static TabExecutor instance;

    private static TabExecutor getInstance(SternalBoardPlugin plugin) {
        if (instance == null) {
            instance = new ModernTabExecutor(plugin);
        }
        return instance;
    }


    public abstract void sendTab(Player player, String header, String footer);

    public static void sendTabList(SternalBoardPlugin plugin, Player player, String header, String footer) {
        getInstance(plugin).sendTab(player, header, footer);
    }

    public String check(String line) {
        return (line == null || line.isEmpty()) ? " " : line;
    }
}