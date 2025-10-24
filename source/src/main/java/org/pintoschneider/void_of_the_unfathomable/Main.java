package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.MainMenu;

import java.io.IOException;
import java.util.Arrays;

public final class Main {
    static void main() throws InterruptedException, IOException {
        final boolean debugMode = Boolean.parseBoolean(System.getProperty("debug", "false"));
        if (debugMode) {
            try (final DebugLogger _ = new DebugLogger()) {
                run();
            } catch (IOException exception) {
                System.err.printf(
                    "Failed to initialize debug logger: %n%s%n%s%n",
                    exception.getMessage(),
                    Arrays.toString(exception.getStackTrace())
                );
            }
        } else {
            run();
        }
    }

    static void run() throws InterruptedException, IOException {
        final Scene scene = new MainMenu();

        try (final Engine engine = new Engine(scene)) {
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
