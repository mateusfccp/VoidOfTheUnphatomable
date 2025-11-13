package org.pintoschneider.void_of_the_unfathomable.engine;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

/**
 * Interface that represents a scene in the game.
 * <p>
 * A scene is responsible for building its UI component, handling key press events, and disposing of resources when no
 * longer needed.
 */
public interface Scene {
    /**
     * A method called whenever the {@link SceneManager} pushes this scene onto the stack.
     */
    default void onEnter() {}

    /**
     * Builds and returns the root UI component for this scene.
     *
     * @return The root {@link Component} of the scene.
     */
    Component build();

    /**
     * Updates the scene.
     * <p>
     * This method is called once per frame, allowing the scene to update its state based on the elapsed time.
     * It should be used for the main scene logic that's not UI or input handling, such as animations or game state
     * updates.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    default void onUpdate(long deltaTime) {}

    /**
     * Handles a key press event.
     * <p>
     * This method is called whenever a key is pressed while this scene is active.
     *
     * @param key The code of the key that was pressed.
     */
    default void onKeyPress(Key key) {}

    /**
     * Disposes of any resources held by the scene.
     * <p>
     * This method is called when the scene is no longer needed, allowing it to clean up resources such as textures,
     * sounds, or other assets.
     */
    default void dispose() {}
}
