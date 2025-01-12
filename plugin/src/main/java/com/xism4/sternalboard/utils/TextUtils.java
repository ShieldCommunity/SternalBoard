package com.xism4.sternalboard.utils;

import com.xism4.sternalboard.SternalBoardHandler;
import com.xism4.sternalboard.SternalBoardPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final String MINI_MESSAGE_HEX = "<color:{color}>";
    private static final Pattern HEX_PATTERN = Pattern.compile("(#|&#)([A-Fa-f0-9]){6}");

    public static String colorize(String text) {
        if (SternalBoardHandler.VersionType.V1_13.isLowerOrEqual()) {
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        text = transformLegacyHex(text);
        text = text.replace("§", "&");

        MiniMessage mm = MiniMessage.miniMessage();
        return LegacyComponentSerializer.legacySection().serialize(mm.deserialize(text));
    }

    public static String transformLegacyHex(String text) {
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        Matcher matcher = HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            result.append(text, lastEnd, matcher.start());

            String hex = matcher.group()
                    .replace("<", "")
                    .replace(">", "")
                    .replace("&", "");

            result.append(MINI_MESSAGE_HEX.replace("{color}", hex));
            lastEnd = matcher.end();
        }

        result.append(text.substring(lastEnd));
        return result.toString();
    }

    public static String processPlaceholders(SternalBoardPlugin plugin, Player player, String text) {
        return plugin.placeholderCheck() ? colorize(PlaceholderAPI.setPlaceholders(player, text)) : colorize(text);
    }

}