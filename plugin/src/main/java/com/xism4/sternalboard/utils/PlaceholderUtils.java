package com.xism4.sternalboard.utils;

import com.xism4.sternalboard.SternalBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}}");

    public static String sanitizeString(Player player, String text) {
        if (SternalBoard.getInstance().getServer().getPluginManager().getPlugin(
                "PlaceholderAPI") != null) {
            return colorize(PlaceholderAPI.setPlaceholders(player, text)
            );
        }
        else {
            return colorize(text);
        }
    }

    public static String colorize(String text) {
        if (Bukkit.getVersion().contains("1.16")) {
            Matcher match = HEX_PATTERN.matcher(text);
            while (match.find()) {
                String color = text.substring(match.start(), match.end());
                text = text.replace(color, ChatColor.of(color) + "");
                match = HEX_PATTERN.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes(
                '&', text
        );
    }

    public static String parsePAPI(Player player, String text) {
        if (SternalBoard.getInstance().getServer().getPluginManager().getPlugin(
                "PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return (colorize(text)
        );
    }
}
