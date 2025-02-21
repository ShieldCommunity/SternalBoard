package com.xism4.sternalboard.manager;

/**
 * Represents a all managers of system.
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
}
