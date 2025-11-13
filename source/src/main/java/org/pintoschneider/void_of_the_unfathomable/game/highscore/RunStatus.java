package org.pintoschneider.void_of_the_unfathomable.game.highscore;

public enum RunStatus {
    FORSAKEN(2),
    COWARD(2),
    DECEASED(1),
    VICTORIOUS(0);

    RunStatus(int priority) {
        this.priority = priority;
    }

    final int priority;
}
