package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.MainMenu;

import java.io.IOException;

public class Main {
    static final int FPS = 30;

    static void main() {
        final Scene scene = new MainMenu();

        try (final Engine engine = new Engine(scene)) {
            engine.waitUntilStopped();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
