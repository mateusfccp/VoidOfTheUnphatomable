package org.pintoschneider.void_of_the_unfathomable;

import org.jline.terminal.Terminal;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.MainMenu;

import java.io.IOException;
import java.util.Arrays;

/**
 * The main application logic that runs the game engine using a JLine terminal.
 *
 * @param terminal The JLine terminal to use for input and output.
 */
public record App(Terminal terminal) implements Runnable {
    @Override
    public void run() {
        final Scene scene;
        try {
            scene = new MainMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (final Engine engine = new Engine(scene, terminal)) {
            engine.waitUntilStopped();
        } catch (Exception exception) {
            System.err.printf(
                "Exception caught in Main:%n%s%n%s%n",
                exception.getMessage(),
                Arrays.toString(exception.getStackTrace())
            );
        }
    }
}
