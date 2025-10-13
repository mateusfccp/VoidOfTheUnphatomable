package org.pintoschneider.void_of_the_unfathomable.ui;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Drawable;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Size;

import java.io.IOException;

public class Engine implements AutoCloseable {
    private final Terminal terminal = TerminalBuilder.builder().build();
    private final NonBlockingReader reader = terminal.reader();
    private final Drawable rootDrawable;
    private Size terminalSize;

    public Engine(Drawable rootDrawable) throws IOException {
        this.rootDrawable = rootDrawable;
        terminal.enterRawMode();

        // Register a signal handler for window resize events
        terminal.handle(Terminal.Signal.WINCH, signal -> {
            updateTerminalSize();
            refresh();
        });

        terminal.puts(Capability.cursor_invisible);

        updateTerminalSize();
        refresh();
    }

    public boolean isAlive() {
        return true; // Placeholder for actual game loop condition
    }

    public void tick() {
        try {
            final int input = reader.read();
            if (input != NonBlockingReader.READ_EXPIRED) {
                refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearScreen() {
        terminal.puts(Capability.clear_screen);
    }

    private void refresh() {
        clearScreen();
        layout();
        draw();
        terminal.flush();
    }

    private void layout() {
        // Root drawable takes the entire terminal size
        final Constraints constraints = Constraints.tight(terminalSize.width(), terminalSize.height());
        rootDrawable.layout(constraints);
    }

    private void draw() {
        final Size drawableSize = rootDrawable.getSize();

        if (drawableSize == null) {
            throw new IllegalStateException("Drawable size is null. Did you forget to call layout()?");
        }

        final Canvas rootCanvas = new Canvas(drawableSize);
        rootDrawable.draw(rootCanvas);
        rootCanvas.writeTo(terminal.writer());
    }

    private void updateTerminalSize() {
        terminalSize = new Size(terminal.getWidth(), terminal.getHeight());
    }

    @Override
    public void close() throws Exception {
        terminal.close();
    }
}
