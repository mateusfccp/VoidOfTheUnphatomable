package org.pintoschneider.void_of_the_unfathomable.game.highscore;

public class HighscoreEntry {
    private final RunStatus status;
    private final int turnCount;
    private final int percentage;

    public HighscoreEntry(RunStatus status, int percentage,  int turnCount) {
        this.status = status;
        this.percentage = percentage;
        this.turnCount = turnCount;
    }

    public RunStatus status() {
        return status;
    }

    public int turnCount() {
        return turnCount;
    }

    public int percentage() {
        return percentage;
    }
}
