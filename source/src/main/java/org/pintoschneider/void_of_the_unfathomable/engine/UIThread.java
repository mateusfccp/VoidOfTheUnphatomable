package org.pintoschneider.void_of_the_unfathomable.engine;

import java.util.Objects;

/**
 * A thread that runs the game UI, updating the engine at a fixed frames per second (FPS).
 */
public final class UIThread extends Thread {
    private final Engine engine;
    private final int fps;
    private long lastNanoTime;
    private volatile long tickCount = 0;
    private volatile long deltaTime = 0;

    /**
     * Creates a UIThread.
     *
     * @param engine The game engine to update.
     * @param fps    The target frames per second.
     */
    UIThread(Engine engine, int fps) {
        this.engine = Objects.requireNonNull(engine);
        this.fps = fps;
    }

    /**
     * Returns the number of ticks that have occurred since the thread started.
     *
     * @return The tick count.
     */
    public long tickCount() {
        return tickCount;
    }

    /**
     * Returns the time in nanoseconds since the last tick.
     *
     * @return The delta time in nanoseconds.
     */
    public long deltaTime() {
        return deltaTime;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final long nanoSeconds = System.nanoTime();

            if (nanoSeconds - lastNanoTime >= 1_000_000_000L / fps) {
                deltaTime = nanoSeconds - lastNanoTime;
                lastNanoTime = nanoSeconds;

                synchronized (this) {
                    tickCount++;
                }

                engine.tick();
            }
        }
    }
}
