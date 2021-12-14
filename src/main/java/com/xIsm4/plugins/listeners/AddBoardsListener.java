package com.xIsm4.plugins.listeners;

import com.xIsm4.plugins.Structure;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;

public class AddBoardsListener implements Listener {

    private final Structure core;

    public AddBoardsListener(Structure core) {
        this.core = core;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        core.getScoreboardManager().setScoreboard(e.getPlayer());
        }
    }
