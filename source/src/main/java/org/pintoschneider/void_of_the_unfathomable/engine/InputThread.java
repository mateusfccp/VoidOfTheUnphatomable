package org.pintoschneider.void_of_the_unfathomable.engine;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Reads input from the NonBlockingReader and puts key codes into a queue.
 * This version uses a state machine to correctly parse escape sequences.
 */
final class InputThread extends Thread {
    private final BindingReader bindingReader;
    private final AtomicReference<Key> lastKey;
    private final KeyMap<Key> keyMap;

    /**
     * Creates an InputThread.
     *
     * @param terminal The terminal to read input from.
     * @param lastKey  The AtomicReference to store the last key read.
     */
    InputThread(Terminal terminal, AtomicReference<Key> lastKey) {
        Objects.requireNonNull(terminal);
        final NonBlockingReader reader = terminal.reader();
        this.bindingReader = new BindingReader(reader);
        this.lastKey = Objects.requireNonNull(lastKey);
        this.keyMap = Key.createKeyMap(terminal);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final Key key = bindingReader.readBinding(keyMap, null, false);

            if (key != null) {
                lastKey.set(key);
            }
        }
    }
}