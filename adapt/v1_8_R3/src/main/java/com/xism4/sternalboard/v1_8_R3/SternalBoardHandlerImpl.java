package com.xism4.sternalboard.v1_8_R3;

import com.xism4.sternalboard.api.scoreboard.ScoreboardHandler;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class SternalBoardHandlerImpl implements ScoreboardHandler {

    // Packet Fields
    private PacketPlayOutScoreboardObjective scoreboardObjective;
    private PacketPlayOutScoreboardDisplayObjective scoreboardDisplayObjective;
    private PacketPlayOutScoreboardScore scoreboardScore;
    private PacketPlayOutScoreboardTeam scoreboardTeam;

    // Scoreboard Fields
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


    }

    @Override
    public void lines(String... lines) {

    }

    @Override
    public void lines(Collection<String> lines) {
        ScoreboardHandler.super.lines(lines);
    }

    @Override
    public String line(int line) {
        return null;
    }

    @Override
    public String[] lines() {
        return new String[0];
    }

    @Override
    public List<String> linesList() {
        return null;
    }

    @Override
    public boolean removeLine(int line) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void update(String title, String... lines) {


    }

    @Override
    public void updatePacket(Player player, Object packet) {


    }

    @Override
    public void sendPacket(Player player, Object packet) {

    }
}
