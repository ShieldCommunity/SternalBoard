package com.xism4.sternalboard.managers.tab.list;

import com.xism4.sternalboard.managers.tab.TabExecutor;
import com.xism4.sternalboard.utils.color.ColorHandler;
import com.xism4.sternalboard.utils.placeholders.PlaceholderParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ModernTabExecutor extends TabExecutor {
    private final JavaPlugin plugin;

    public ModernTabExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendTab(Player player, String header, String footer) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            player.setPlayerListHeaderFooter(
                    ColorHandler.convert(
                            PlaceholderParser.parse(player, check(header))
                    ),
                    ColorHandler.convert(
                            PlaceholderParser.parse(player, check(footer))
                    )
            );
            return;
        }
        player.setPlayerListHeaderFooter(
                ColorHandler.convert(check(header)),
                ColorHandler.convert(check(footer))
        );
    }
}
