package org.pintoschneider.void_of_the_unfathomable.game;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;

/**
 * A namespace class that holds color constants used throughout the game.
 */
public abstract class Colors {
    private Colors() {
        // Prevent instantiation
    }

    /**
     * The color used to indicate damage taken by entities.
     */
    public static final Color DAMAGE = new Color(237, 119, 90);
}
