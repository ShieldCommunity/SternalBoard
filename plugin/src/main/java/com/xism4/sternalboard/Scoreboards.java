package com.xism4.sternalboard;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class Scoreboards {

    public static void updateFromSection(SternalBoardHandler handler, ConfigurationSection section) {
        if (section == null) {
            return;
        }

        String title = section.getString("title");
        List<String> lines = section.getStringList("lines");

        if (title == null) {
            title = handler.getTitle();
        }

        if (lines.isEmpty()) {
            lines = handler.getLines();
        }

        handler.updateTitle(title);
        handler.updateLines(lines);
    }

}
