package org.pintoschneider.void_of_the_unfathomable.game.engine;

import java.util.concurrent.CompletableFuture;

record SceneExecution(Scene scene, CompletableFuture<Object> future) {
    SceneExecution(Scene scene) {
        this(scene, new CompletableFuture<>());
    }
}
