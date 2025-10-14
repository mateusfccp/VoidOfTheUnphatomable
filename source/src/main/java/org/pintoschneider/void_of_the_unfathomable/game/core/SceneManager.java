package org.pintoschneider.void_of_the_unfathomable.game.core;

import java.util.Stack;

public class SceneManager {
    private final Stack<Scene> scenes;

    public SceneManager(Scene initialScene) {
        scenes = new Stack<>();
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
