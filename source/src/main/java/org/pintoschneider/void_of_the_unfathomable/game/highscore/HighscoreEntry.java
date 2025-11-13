package org.pintoschneider.void_of_the_unfathomable.game.highscore;

/**
 * A class representing a high-score entry.
 */
public class HighscoreEntry {
    private final RunStatus status;
    private final int turnCount;
    private final int percentage;

    public HighscoreEntry(RunStatus status, int percentage,  int turnCount) {
        this.status = status;
        this.percentage = percentage;
        this.turnCount = turnCount;
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
    public int turnCount() {
        return turnCount;
    }
}
