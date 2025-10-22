package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

@FunctionalInterface
public interface TurnStep {
    /**
     * Executes the action for the turn.
     * <p>
     * Returns whether the action was successful or not.
     *
     * @param lastTurnResult The result of the last turn step executed, or null if this is the first step.
     * @return True if the action was successful, false otherwise.
     */
    boolean execute(Boolean lastTurnResult);
}
