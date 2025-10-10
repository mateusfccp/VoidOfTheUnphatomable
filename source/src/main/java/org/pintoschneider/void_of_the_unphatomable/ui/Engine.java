package org.pintoschneider.void_of_the_unphatomable.ui;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;
import org.pintoschneider.void_of_the_unphatomable.ui.core.Drawable;

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
        terminal.puts(InfoCmp.Capability.clear_screen);
    }

    private void refresh() {
        clearScreen();
        layout();
        draw();
        terminal.flush();
    }

    private void layout() {
        rootDrawable.layout(terminalSize);
    }

    private void draw() {
        final Size drawableSize = rootDrawable.getSize();

        if (drawableSize == null) {
            throw new IllegalStateException("Drawable size is null. Did you forget to call layout()?");
        }

        for (int y = 0; y < drawableSize.getHeight(); y++) {
            for (int x = 0; x < drawableSize.getWidth(); x++) {
                final Character character = rootDrawable.draw(x, y);
                if (character != null) {
                    drawAt(x, y, character.toString());
                }
            }
        }
    }

    private void drawAt(int x, int y, String string) {
        final int stringEndX = x + string.length();
        final int overflowX = Math.max(0, stringEndX - terminalSize.getWidth());

        terminal.puts(InfoCmp.Capability.cursor_address, y, x);
        if (overflowX > 0) {
            final String trimmedString = string.substring(0, string.length() - overflowX - 1) + '…';
            terminal.writer().print(trimmedString);
        } else {
            terminal.writer().print(string);
        }
    }

    private void updateTerminalSize() {
        terminalSize = new Size(terminal.getWidth(), terminal.getHeight());
    }

    @Override
    public void close() throws Exception {
        terminal.close();
    }
}
