package com.xism4.sternalboard.managers.tab.list;

import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.TabExecutor;
import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.entity.Player;

public class ModernTabExecutor extends TabExecutor {
    private final SternalBoardPlugin plugin;

    public ModernTabExecutor(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendTab(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(
                TextUtils.processPlaceholders(plugin, player, check(header)),
                TextUtils.processPlaceholders(plugin, player, check(footer))
        );
    }
}
