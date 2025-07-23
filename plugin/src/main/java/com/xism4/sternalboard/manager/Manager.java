package com.xism4.sternalboard.manager;

import org.bukkit.entity.Player;

/**
 * Represents all managers of the system.
 *
 * @since 3.0
 */

public interface Manager {

    /**
     * Initialize the manager. This method should be called only once.
     */
    void init();

    /**
     * Reload the manager. This method should be called when the manager needs to be reloaded.
     */
    void reload();

    /*
     * Toggles the scoreboard. This method should be called only at manager
     */
    void toggle(Player player);
}
