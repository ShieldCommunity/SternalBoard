package com.xism4.sternalboard.api.scoreboard;

import java.util.Collection;

public interface ScoreboardHandler {

    boolean title(String title);

    boolean line(int line, String text);

    boolean removeLine(int line);

    boolean clear();

    default boolean stop() {
        return clear();
    }

    boolean lines(String... lines);

    boolean update(String title, String... lines);

    default boolean lines(Collection<String> lines) {
        return lines(lines.toArray(new String[0]));
    }
}
