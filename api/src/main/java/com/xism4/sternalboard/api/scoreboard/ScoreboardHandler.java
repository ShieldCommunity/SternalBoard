package com.xism4.sternalboard.api.scoreboard;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface ScoreboardHandler {

    void title(String title);

    String title();

    void line(int line, String text);

    void lines(String... lines);

    default void lines(Collection<String> lines) {
    }

    String line(int line);

    String[] lines();

    List<String> linesList();

    boolean removeLine(int line);

    void clear();

    void update(String title, String... lines);

    void updatePacket(Player player, Object packet);

    void sendPacket(Player player, Object packet);
}
