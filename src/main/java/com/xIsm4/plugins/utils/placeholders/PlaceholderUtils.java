package com.xIsm4.plugins.utils.placeholders;

import com.xIsm4.plugins.Main;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderUtils {

    public static String setPlaceholders(OfflinePlayer player, String text) {
        if(Main.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, text));
        } else {
            return ChatColor.translateAlternateColorCodes('&', text);
        }
    }
}
