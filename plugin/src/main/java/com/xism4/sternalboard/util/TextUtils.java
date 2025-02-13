package com.xism4.sternalboard.util;

import com.xism4.sternalboard.SternalBoardPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public final class TextUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("(#|&#)([A-Fa-f0-9]{6})");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) return "";

        return ChatColor.translateAlternateColorCodes('&',
                LEGACY_SERIALIZER.serialize(
                        MINI_MESSAGE.deserialize(transformLegacyHex(text).replaceAll("§", "&"))
                )
        );
    }

    public static String transformLegacyHex(String text) {
        return HEX_PATTERN.matcher(text).replaceAll(matcher -> "<color:" + matcher.group(2) + ">");
    }

    public static String processPlaceholders(SternalBoardPlugin plugin, Player player, String text) {
        return colorize(plugin.placeholderCheck() ? PlaceholderAPI.setPlaceholders(player, text) : text);
    }
}
