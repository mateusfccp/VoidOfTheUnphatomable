package org.pintoschneider.void_of_the_unfathomable.game.core;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.io.IOException;
import java.io.PrintWriter;

public class Engine implements AutoCloseable {
    private final Terminal terminal = TerminalBuilder.builder().system(true).build();
    private final NonBlockingReader reader = terminal.reader();
    private final PrintWriter writer = terminal.writer();
    private Size terminalSize;
    private final Scene scene;
    private long lastNanoTime;

    public Engine(Scene initialScene) throws IOException {
        this.scene = initialScene;
        terminal.enterRawMode();

        // Register a signal handler for window resize events
        terminal.handle(Terminal.Signal.WINCH, _ -> {
            updateTerminalSize();
            refresh();
        });

        terminal.puts(Capability.cursor_invisible);

        lastNanoTime = System.nanoTime();

        updateTerminalSize();
        refresh();
    }

    public boolean isAlive() {
        return true; // Placeholder for actual game loop condition
    }

    public void tick() {
        try {
            final int c = reader.read();
            if (c != NonBlockingReader.READ_EXPIRED && c != NonBlockingReader.EOF) {
                scene.onKeyPress(c);
            }

            refresh();
            lastNanoTime = System.nanoTime();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearScreen() {
        terminal.puts(Capability.clear_screen);
    }

    private void refresh() {
        clearScreen();
        drawScene();
    }

    private void drawScene() {
        final Component rootComponent = scene.build(System.nanoTime() - lastNanoTime);
        rootComponent.layout(Constraints.tight(terminalSize.width(), terminalSize.height()));

        final Canvas rootCanvas = new Canvas(rootComponent.size(), rootComponent);
        rootComponent.draw(rootCanvas);
        rootCanvas.writeTo(writer);
        writer.flush();
    }

    private void updateTerminalSize() {
        terminalSize = new Size(terminal.getWidth(), terminal.getHeight());
    }

    @Override
    public void close() throws IOException {
        terminal.close();
    }
}
