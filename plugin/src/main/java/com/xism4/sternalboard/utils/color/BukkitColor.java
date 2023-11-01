package com.xism4.sternalboard.utils.color;

import org.bukkit.ChatColor;

public class BukkitColor extends ColorHandler {
    @Override
    public String execute(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}

