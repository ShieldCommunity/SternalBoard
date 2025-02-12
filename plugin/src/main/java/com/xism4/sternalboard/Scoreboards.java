package com.xism4.sternalboard;

import com.xism4.sternalboard.utils.TextUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Scoreboards {

    public static void updateFromSection(SternalBoardPlugin plugin, SternalBoard handler, ConfigurationSection section) {
        if (section == null) return;

        var title = Objects.requireNonNullElse(section.getString("title"), handler.getTitle());
        var lines = section.getStringList("lines").isEmpty() ? handler.getLines() : section.getStringList("lines");

        var player = handler.getPlayer();

        var processedLines = lines.stream()
                .map(line -> TextUtils.processPlaceholders(plugin, player, line))
                .toList();

        handler.updateTitle(TextUtils.processPlaceholders(plugin, player, title));
        handler.updateLines(processedLines);
    }
}
