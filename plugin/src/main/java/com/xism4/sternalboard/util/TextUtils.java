package com.xism4.sternalboard.util;

import com.xism4.sternalboard.SternalBoardPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final String MINI_MESSAGE_HEX = "<color:{color}>";
    private static final Pattern HEX_PATTERN = Pattern.compile("(#|&#)([A-Fa-f0-9]){6}");

    private static final int SERVER_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]
    );

    public static String colorize(String text) {

        if (SERVER_VERSION < 16) {
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        text = transformLegacyHex(text);
        text = text.replaceAll("§", "&");

        MiniMessage mm = MiniMessage.miniMessage();
        text = LegacyComponentSerializer.legacySection().serialize(mm.deserialize(text));

        return ChatColor.translateAlternateColorCodes(
                '&', text
        );
    }

    public static String transformLegacyHex(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);

        while (matcher.find()) {
            String hex = matcher.group()
                    .replace("<", "")
                    .replace(">", "")
                    .replace("&", "");

            text = text.replace(matcher.group(), MINI_MESSAGE_HEX.replace("{color}", hex));
        }

        return text;
    }

    public static String processPlaceholders(SternalBoardPlugin plugin, Player player, String text) {
        return plugin.placeholderCheck() ? colorize(PlaceholderAPI.setPlaceholders(player, text)) : colorize(text);
    }
}