package org.pintoschneider.void_of_the_unfathomable.engine;

import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Reads input from the NonBlockingReader and puts key codes into a queue.
 * This version uses a state machine to correctly parse escape sequences.
 */
final class InputThread extends Thread {
    private static final int ESCAPE_CHAR = 27;
    private static final int BRACKET_CHAR = 91;
    private final NonBlockingReader reader;
    private final AtomicReference<Key> lastKey;
    private final ArrayList<Character> sequenceBuffer = new ArrayList<>(3);
    private State currentState = State.NORMAL;

    /**
     * Creates an InputThread.
     *
     * @param reader  The NonBlockingReader to read input from.
     * @param lastKey The AtomicReference to store the last key read.
     */
    InputThread(NonBlockingReader reader, AtomicReference<Key> lastKey) {
        this.reader = Objects.requireNonNull(reader);
        this.lastKey = Objects.requireNonNull(lastKey);
    }

    @Override
    public void run() {
        try {
            final CharBuffer buffer = CharBuffer.allocate(16);

            while (!Thread.currentThread().isInterrupted()) {
                // TODO(mateusfccp): We may want to implement a timeout mechanism here to reset the state machine and deal with incomplete sequences.
                int readCount = reader.read(buffer);

                if (readCount > 0) {
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        final char ch = buffer.get();

                        switch (currentState) {
                            case NORMAL:
                                if (ch == ESCAPE_CHAR) {
                                    sequenceBuffer.add(ch);
                                    currentState = State.ESCAPE;
                                } else {
                                    sendKey(ch);
                                }
                                break;

                            case ESCAPE:
                                if (ch == BRACKET_CHAR) {
                                    sequenceBuffer.add(ch);
                                    currentState = State.BRACKET;
                                } else {
                                    // Not a CSI sequence. Send the buffered ESC key.
                                    sendBufferedKey();

                                    // Process the current char normally
                                    if (ch == ESCAPE_CHAR) {
                                        sequenceBuffer.add(ch);
                                        // currentState remains State.ESCAPE
                                    } else {
                                        sendKey(ch);
                                        currentState = State.NORMAL;
                                    }
                                }
                                break;

                            case BRACKET:
                                // Final char of the sequence
                                sequenceBuffer.add(ch);
                                sendBufferedKey();
                                currentState = State.NORMAL;
                                break;
                        }
                    }
                    buffer.clear();
                }
            }
        } catch (IOException exception) {
            System.err.printf(
                "Exception caught in InputThread:%n%s%n%s%n",
                exception.getMessage(),
                Arrays.toString(exception.getStackTrace())
            );
        }
    }

    private void sendKey(char ch) {
        final char[] sequence = new char[]{ch};
        final Key key = Key.parse(sequence);
        lastKey.set(key);
    }

    private void sendBufferedKey() {
        if (sequenceBuffer.isEmpty()) {
            return;
        }

        char[] finalSequence = new char[sequenceBuffer.size()];
        for (int i = 0; i < sequenceBuffer.size(); i++) {
            finalSequence[i] = sequenceBuffer.get(i);
        }

        lastKey.set(Key.parse(finalSequence));
        sequenceBuffer.clear();
    }

    /**
     * The states of the input parser state machine.
     */
    private enum State {
        /**
         * Normal state, reading regular characters.
         */
        NORMAL,
        /**
         * Escape character received, expecting further input.
         */
        ESCAPE,
        /**
         * Bracket character received after escape, expecting final character.
         */
        BRACKET
    }
}