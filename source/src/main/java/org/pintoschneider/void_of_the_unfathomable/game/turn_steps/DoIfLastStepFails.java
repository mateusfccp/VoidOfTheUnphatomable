package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

/**
 * A {@link TurnStep} that only executes if the last step failed.
 * <p>
 * If this is the first step, it will always execute.
 */
public class DoIfLastStepFails implements TurnStep {
    private final TurnStep step;

    /**
     * Creates a new DoIfLastStepSucceeds action for the given step.
     *
     * @param step The step to execute if the last step was successful.
     */
    public DoIfLastStepFails(TurnStep step) {
        this.step = step;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        if (lastTurnResult == null || !lastTurnResult) {
            return step.execute(lastTurnResult);
        } else {
            return false;
        }
    }
}
