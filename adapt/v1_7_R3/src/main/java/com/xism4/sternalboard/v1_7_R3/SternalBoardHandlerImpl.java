package com.xism4.sternalboard.v1_7_R3;

import com.google.common.collect.Lists;
import com.xism4.sternalboard.api.scoreboard.ScoreboardHandler;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SternalBoardHandlerImpl implements ScoreboardHandler {

    private PacketPlayOutScoreboardObjective scoreboardObjective;
    private PacketPlayOutScoreboardDisplayObjective displayObjective;
    private PacketPlayOutScoreboardScore scoreboardScore;
    private PacketPlayOutScoreboardTeam scoreboardTeam;

    private String title;
    private List<String> lines;


    @Override
    public void title(String title) {
        this.title = title;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void line(int line, String text) {
        if (lines == null) {
            lines = Collections.singletonList(text);
            return;
        }

        if (lines.size() < line) {
            lines = Lists.newArrayList(lines);
            lines.add(text);
            return;
        }

        lines.set(line, text);
    }

    @Override
    public void lines(String... lines) {
        this.lines = Arrays.asList(lines);
    }

    @Override
    public void lines(Collection<String> lines) {
        this.lines = Lists.newArrayList(lines);
    }

    @Override
    public String line(int line) {
        return lines.get(line);
    }

    @Override
    public String[] lines() {
        return lines.toArray(new String[0]);
    }

    @Override
    public List<String> linesList() {
        return lines;
    }

    @Override
    public boolean removeLine(int line) {
        return lines.remove(line) != null;
    }

    @Override
    public void clear() {
        title = null;
        lines = null;
    }

    @Override
    public void update(String title, String... lines) {
        this.title = title;
        this.lines = Arrays.asList(lines);

    }

    @Override
    public synchronized void updatePacket(Player player, Object packet) {

    }

    @Override
    public void sendPacket(Player player, Object packet) {

    }
}
