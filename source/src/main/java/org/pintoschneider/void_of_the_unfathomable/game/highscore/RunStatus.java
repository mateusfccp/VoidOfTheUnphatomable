package org.pintoschneider.void_of_the_unfathomable.game.highscore;

/**
 * Enum representing status of the game state
 */
public enum RunStatus {
    /**
     * The player abandons the game through the escape menu
     */
    FORSAKEN(2),

    /**
     * The player escapes the Void through the ladder without the Resounding Core like a coward
     */
    COWARD(2),

    /**
     * The player dies in battle
     */
    DECEASED(1),

    /**
     * The player escapes the Void through the ladder with the Resounding Core saving the world from the Void
     */
    VICTORIOUS(0);

    private final int priority;
    RunStatus(int priority) {
        this.priority = priority;
    }

    /**
     * The priority of the RunState
     *
     * @return The priority of the RunState
     */
    public final int priority() {
        return priority;
    }
}
