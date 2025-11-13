package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;

/**
 * A {@link TurnStep} that moves an entity towards a position.
 */
public final class MoveTowards implements TurnStep {
    private final Entity<?> entity;
    private final Offset position;

    /**
     * Creates a new MoveTowards action for the given entity.
     *
     * @param entity   The entity that will move towards the position.
     * @param position The position to move towards.
     */
    public MoveTowards(Entity<?> entity, Offset position) {
        this.entity = entity;
        this.position = position;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        return entity.moveTowards(position);
    }
}
