package com.xIsm4.plugins.utils.placeholders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xIsm4.plugins.Structure;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class PlaceholderUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}}");
    //Renember that only works with Legacy (U can use birdflop web to test it)

    public static String sanitizeString(Player player, String text) {
        if (Structure.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return colorize(PlaceholderAPI.setPlaceholders(player, text));

        } else {
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
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    //Parse PlaceholderAPI variables
    public static String parsePAPI(Player player, String text){
        if (Structure.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return (colorize(text));
    }
}
