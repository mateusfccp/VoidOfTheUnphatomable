package org.pintoschneider.void_of_the_unfathomable.game.engine;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A manager for handling scenes in the game engine.
 * <p>
 * It uses a stack-like structure to manage scenes, allowing for pushing and popping scenes as needed. The top of the
 * stack representes the current active scene.
 * <p>
 * When the last scene is popped, the engine should terminate.
 */
public final class SceneManager {
    private final ConcurrentLinkedDeque<SceneExecution> scenes;

    /**
     * Creates a new SceneManager with the specified initial scene.
     *
     * @param initialScene The initial scene to be added to the manager.
     */
    public SceneManager(Scene initialScene) {
        scenes = new ConcurrentLinkedDeque<>();
        final SceneExecution execution = new SceneExecution(initialScene);
        scenes.push(execution);
    }

    /**
     * Gets the current active scene.
     * <p>
     * The current scene is the one at the top of the stack.
     *
     * @return The current active scene.
     */
    public Scene currentScene() {
        return scenes.peek().scene();
    }

    /**
     * Checks if there are any scenes in the manager.
     * <p>
     * There should be at least one scene; if there are none, the manager is considered done and the engine should
     * terminate.
     *
     * @return True if there are scenes in the manager, false otherwise.
     */
    public boolean hasScene() {
        return !scenes.isEmpty();
    }

    /**
     * Pushes a new scene onto the stack, making it the current active scene.
     *
     * @param scene The scene to be pushed onto the stack.
     * @return a {@link CompletableFuture} that will be completed when the scene is popped.
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> push(Scene scene) {
        final SceneExecution execution = new SceneExecution(scene);
        scenes.push(execution);
        scene.onEnter();
        return (CompletableFuture<T>) execution.future();
    }

    /**
     * Pops the current active scene from the stack.
     * <p>
     * The popped scene is disposed of to free up resources, and the next scene in the stack becomes the current active
     * scene.
     */
    public void pop() {
        pop(null);
    }

    /**
     * Pops the current active scene from the stack.
     * <p>
     * The popped scene is disposed of to free up resources, and the next scene in the stack becomes the current active
     * scene.
     *
     * @param result The result to be passed to the waiting call-site.
     */
    public void pop(Object result) {
        final SceneExecution execution = scenes.pop();
        execution.scene().dispose();
        execution.future().complete(result);
    }

    /**
     * Gets a stack of all scenes currently managed by the SceneManager.
     *
     * @return A stack containing all scenes.
     */
    public Stack<Scene> scenes() {
        final Stack<Scene> stack = new Stack<>();
        for (SceneExecution execution : scenes) {
            stack.add(execution.scene());
        }
        return stack;
    }
}
