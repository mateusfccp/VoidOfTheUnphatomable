package org.pintoschneider.void_of_the_unfathomable.game.engine;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

/**
 * Interface representing a scene in the game.
 * <p>
 * A scene is responsible for building its UI component, handling key press events, and disposing of resources when no
 * longer needed.
 */
public interface Scene {
    /**
     * Builds and returns the root UI component for this scene.
     *
     * @return The root {@link Component} of the scene.
     */
    Component build(Context context);

    /**
     * Handles a key press event.
     * <p>
     * This method is called whenever a key is pressed while this scene is active.
     *
     * @param context The engine context.
     * @param keyCode The code of the key that was pressed.
     */
    default void onKeyPress(Context context, int keyCode) {
    }

    /**
     * Disposes of any resources held by the scene.
     * <p>
     * This method is called when the scene is no longer needed, allowing it to clean up resources such as textures,
     * sounds, or other assets.
     */
    default void dispose() {
    }
}
