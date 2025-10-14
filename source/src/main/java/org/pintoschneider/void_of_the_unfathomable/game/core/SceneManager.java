package org.pintoschneider.void_of_the_unfathomable.game.core;

import java.util.concurrent.ConcurrentLinkedDeque;

public class SceneManager {
    private final ConcurrentLinkedDeque<Scene> scenes;

    public SceneManager(Scene initialScene) {
        scenes = new ConcurrentLinkedDeque<>();
        scenes.push(initialScene);
    }

    public Scene currentScene() {
        return scenes.peek();
    }

    public boolean hasScene() {
        return !scenes.isEmpty();
    }

    public void push(Scene scene) {
        scenes.push(scene);
    }

    public void pop() {
        Scene currentScene = scenes.pop();
        currentScene.dispose();
    }
}
