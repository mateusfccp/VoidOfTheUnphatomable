package org.pintoschneider.void_of_the_unfathomable.engine;

import org.pintoschneider.void_of_the_unfathomable.core.Size;

/**
 * The engine context that provides access to engine services and information.
 */
public interface Context {
    /**
     * Gets the scene manager responsible for managing scenes in the game.
     *
     * @return The scene manager.
     */
    SceneManager sceneManager();

    /**
     * Gets the time elapsed since the last frame in nanoseconds.
     *
     * @return The delta time in nanoseconds.
     */
    long deltaTime();

    /**
     * Gets the number of ticks that have occurred since the engine started.
     *
     * @return The tick count.
     */
    long tickCount();

    /**
     * Gets the current size of the game window.
     *
     * @return The size of the game window.
     */
    Size size();
}
