package com.xism4.sternalboard.scoreboard;

import com.xism4.sternalboard.api.scoreboard.ScoreboardHandler;
import com.xism4.sternalboard.v1_8_R3.SternalBoardHandlerImpl;
import org.bukkit.Bukkit;

public class BoardHandlerFactory {

    public static ScoreboardHandler create() {
        final String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        Bukkit.getLogger().info("VERSION: " + version);

        return new SternalBoardHandlerImpl();
    }

}
