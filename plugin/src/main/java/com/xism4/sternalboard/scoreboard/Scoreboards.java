package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.api.SternalBoard;
import com.xism4.sternalboard.util.TextUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public final class Scoreboards {

    private Scoreboards() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static void updateFromSection(
            final SternalBoard board,
            final ConfigurationSection section
    ) {
        if (section == null) return;

        var title = Objects.requireNonNullElse(section.getString("title"), board.getTitle());
        var lines = section.getStringList("lines").isEmpty() ? board.getLines() : section.getStringList("lines");

        var player = board.getPlayer();

        var processedLines = lines.stream()
                .map(line -> TextUtils.processPlaceholders(player, line))
                .toList();

        board.updateTitle(TextUtils.processPlaceholders(player, title));
        board.updateLines(processedLines);
    }
}
