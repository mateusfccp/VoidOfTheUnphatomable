package org.pintoschneider.void_of_the_unfathomable.game.highscore;

import org.pintoschneider.void_of_the_unfathomable.game.scenes.InGame;

import java.time.LocalDateTime;

/**
 * A class that represents a high-score entry.
 */
public class HighscoreEntry {
    private final RunStatus status;
    private final int percentage;
    private final long turnCount;
    private final LocalDateTime creationTime;

    /**
     * Creates a new HighscoreEntry with the given parameters.
     *
     * @param status     The RunStatus of the high-score.
     * @param percentage The percentage of the game completed.
     * @param turnCount  The amount of turns taken.
     */
    public HighscoreEntry(RunStatus status, int percentage, long turnCount) {
        this.status = status;
        this.percentage = percentage;
        this.turnCount = turnCount;
        this.creationTime = LocalDateTime.now();
    }

    /**
     * Creates a new HighscoreEntry from the given InGame instance and RunStatus.
     *
     * @param game   The InGame instance to extract data from.
     * @param status The RunStatus of the high-score.
     * @return A new HighscoreEntry instance.
     */
    public static HighscoreEntry fromGame(InGame game, RunStatus status) {
        return new HighscoreEntry(
            status,
            game.progress(),
            game.turnCount()
        );
    }

    /**
     * Gets the RunStatus of the High-score.
     *
     * @return The RunStatus of the High-score.
     */
    public RunStatus status() {
        return status;
    }

    /**
     * Gets the percentage of the game the player has completed.
     *
     * @return The percentage of the game the player has completed.
     */
    public int percentage() {
        return percentage;
    }

    /**
     * Gets the amount of turns the player has taken.
     *
     * @return The amount of turns the player took.
     */
    public long turnCount() {
        return turnCount;
    }

    /**
     * Gets the creation time of the high-score entry.
     *
     * @return The creation time of the high-score entry.
     */
    public LocalDateTime creationTime() {
        return creationTime;
    }
}
