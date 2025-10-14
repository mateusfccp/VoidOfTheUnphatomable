package org.pintoschneider.void_of_the_unfathomable.game.core;

/**
 * The engine context that provides access to engine services and information.
 */
public interface Context {
    /**
     * Gets the scene manager responsible for managing scenes in the game.
     * @return The scene manager.
     */
    SceneManager sceneManager();

    /**
     * Gets the time elapsed since the last frame in nanoseconds.
     *
     * @return The delta time in nanoseconds.
     */
    long deltaTime();
}
