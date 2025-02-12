package com.xism4.sternalboard.managers.tab.list;

import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import com.xism4.sternalboard.managers.tab.TabExecutor;
import com.xism4.sternalboard.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ModernTabExecutor extends TabExecutor {
    private final SternalBoardPlugin plugin;

    public ModernTabExecutor(SternalBoardPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendTab(Player player, String header, String footer) {
        if (!SternalBoardHandler.VersionType.V1_17.isHigherOrEqual()) return;

        Component headerComponent = Component.text(TextUtils.processPlaceholders(plugin, player, check(header)));
        Component footerComponent = Component.text(TextUtils.processPlaceholders(plugin, player, check(footer)));

        player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
    }
}
