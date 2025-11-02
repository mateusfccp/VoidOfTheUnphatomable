package org.pintoschneider.void_of_the_unfathomable.animation;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Ticker;

import java.time.Duration;

/**
 * An animation that can be played over a specified duration.
 * <p>
 * The animation tracks its progress from 0.0 to 1.0, where 0.0 means the animation has not started, and 1.0 means the
 * animation has completed.
 */
public class Animation {
    final private Ticker ticker;
    private final Duration duration;
    private boolean playing;
    private Duration elapsedTime;
    private double progress;

    /**
     * Creates a new animation with the specified duration.
     *
     * @param duration The duration of the animation.
     */
    public Animation(Duration duration) {
        this.duration = duration;
        ticker = Engine.context().createTicker(this::update);
        progress = 0;
    }

    public static Animation repeating(Duration duration) {
        return new _RepeatingAnimation(duration);
    }

    /**
     * Plays the animation from the beginning.
     */
    public void play() {
        elapsedTime = Duration.ZERO;
        playing = true;
        progress = 0;
    }

    protected void update(Duration duration) {
        if (playing) {
            elapsedTime = elapsedTime.plus(duration);
            progress = (double) elapsedTime.toNanos() / this.duration.toNanos();

            if (progress >= 1.0) {
                progress = 1.0;
                playing = false;
            }
        }
    }

    /**
     * Checks if the animation is currently playing.
     *
     * @return True if the animation is playing, false otherwise.
     */
    public boolean playing() {
        return playing;
    }

    /**
     * Gets the progress of the animation.
     * <p>
     * A value between 0.0 and 1.0 will be returned, where 0.0 means the animation has not started,
     * and 1.0 means the animation has completed.
     *
     * @return The progress of the animation.
     */
    public double progress() {
        return progress;
    }

    /**
     * Stops the animation and resets its progress.
     */
    public void stop() {
        playing = false;
        progress = 0;
    }

    /**
     * Disposes the animation and releases any resources associated with it.
     */
    public void dispose() {
        ticker.dispose();
    }
}

final class _RepeatingAnimation extends Animation {
    public _RepeatingAnimation(Duration duration) {
        super(duration);
    }

    @Override
    protected void update(Duration duration) {
        super.update(duration);
        if (progress() >= 1.0) {
            play();
        }
    }
}