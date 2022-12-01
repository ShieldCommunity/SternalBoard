package com.xism4.sternalboard;

import com.xism4.sternalboard.utils.TextUtils;
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

        lines.replaceAll(line -> TextUtils.processPlaceholders(handler.getPlayer(), line));

        handler.updateTitle(TextUtils.processPlaceholders(handler.getPlayer(), title));
        handler.updateLines(lines);
    }

}
