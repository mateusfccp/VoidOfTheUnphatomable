package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

/**
 * A {@link TurnStep} that only executes if the last step was successful.
 * <p>
 * If this is the first step, it will always execute.
 */
public class DoIfLastStepSucceeds implements TurnStep {
    final TurnStep step;

    /**
     * Creates a new DoIfLastStepSucceeds action for the given step.
     *
     * @param step The step to execute if the last step was successful.
     */
    public DoIfLastStepSucceeds(TurnStep step) {
        this.step = step;
    }


    @Override
    public boolean execute(Boolean lastTurnResult) {
        if (lastTurnResult == null || lastTurnResult) {
            return step.execute(lastTurnResult);
        } else {
            return false;
        }

    }
}
