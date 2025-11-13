package org.pintoschneider.void_of_the_unfathomable;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.MainMenu;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * The entry point of the application.
 */
public final class Main {
    /**
     * Indicates whether the application is running in debug mode.
     */
    public static final boolean debugMode = Boolean.parseBoolean(System.getProperty("debug", "false"));

    static void main() throws IOException {
        if (debugMode) {
            try (final DebugLogger ignored = new DebugLogger()) {
                run();
            } catch (IOException exception) {
                System.err.printf(
                    "Failed to initialize debug logger: %n%s%n%s%n",
                    exception.getMessage(),
                    Arrays.toString(exception.getStackTrace())
                );
            }
        } else {
            final var nullStream = new PrintStream(OutputStream.nullOutputStream());
            System.setOut(nullStream);
            System.setErr(nullStream);
            run();
        }
    }

    static void run() throws IOException {
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
