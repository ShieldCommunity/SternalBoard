package com.xism4.sternalboard.v1_8_R3;

import com.xism4.sternalboard.api.scoreboard.ScoreboardHandler;

import java.util.Collection;

public class SternalBoardHandlerImpl implements ScoreboardHandler {
    
    @Override
    public boolean title(String title) {
        return false;
    }

    @Override
    public boolean line(int line, String text) {
        return false;
    }

    @Override
    public boolean removeLine(int line) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public boolean stop() {
        return ScoreboardHandler.super.stop();
    }

    @Override
    public boolean lines(String... lines) {
        return false;
    }

    @Override
    public boolean update(String title, String... lines) {
        return false;
    }

    @Override
    public boolean lines(Collection<String> lines) {
        return ScoreboardHandler.super.lines(lines);
    }
}
