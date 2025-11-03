package org.pintoschneider.void_of_the_unfathomable.engine;

import java.util.Deque;
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
    private final Runnable onDone;
    private final Deque<SceneExecution> scenes = new ConcurrentLinkedDeque<>();

    /**
     * Creates a new SceneManager with the specified initial scene.
     *
     * @param initialScene The initial scene to be added to the manager.
     */
    public SceneManager(Scene initialScene, Runnable onDone) {
        final SceneExecution execution = new SceneExecution(initialScene);
        scenes.push(execution);

        this.onDone = onDone;
    }

    /**
     * Gets the current active scene.
     * <p>
     * The current scene is the one at the top of the stack.
     *
     * @return The current active scene.
     */
    public Scene currentScene() {
        assert scenes.peek() != null;
        return scenes.peek().scene();
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

        if (scenes.isEmpty()) {
            onDone.run();
        }
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

record SceneExecution(Scene scene, CompletableFuture<Object> future) {
    SceneExecution(Scene scene) {
        this(scene, new CompletableFuture<>());
    }
}
