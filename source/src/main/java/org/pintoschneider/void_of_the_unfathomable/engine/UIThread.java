package org.pintoschneider.void_of_the_unfathomable.engine;

import java.util.Objects;

/**
 * A thread that runs the game UI, updating the engine at a fixed frames per second (FPS).
 */
public final class UIThread extends Thread {
    private static final int BUFFER_TIME = 5_000_000;
    private final Engine engine;
    private final int fps;
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
        final long optimalTime = 1_000_000_000L / fps;
        long lastNanoTime = System.nanoTime();

        while (!Thread.currentThread().isInterrupted()) {
            long startTime = System.nanoTime();

            // Calculate time passed since last frame
            long now = System.nanoTime();
            deltaTime = now - lastNanoTime;
            lastNanoTime = now;

            synchronized (this) {
                tickCount++;
            }

            engine.tick();

            // Calculate how much time to wait to maintain target FPS
            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;
            long timeRemaining = optimalTime - timeTaken;

            if (timeRemaining > 0) {
                try {
                    if (timeRemaining > BUFFER_TIME) {
                        Thread.sleep((timeRemaining - BUFFER_TIME) / 1_000_000);
                    }

                    // Spin-lock for the remaining time to ensure precision
                    while (System.nanoTime() - startTime < optimalTime) {
                        Thread.onSpinWait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
