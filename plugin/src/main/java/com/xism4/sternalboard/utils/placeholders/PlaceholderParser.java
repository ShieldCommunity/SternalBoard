package com.xism4.sternalboard.utils.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderParser {
    public static String parse(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }
}


