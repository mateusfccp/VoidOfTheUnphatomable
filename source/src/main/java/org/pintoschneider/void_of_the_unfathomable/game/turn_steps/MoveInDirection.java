package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;

/**
 * A {@link TurnStep} that moves in a specified direction.
 */
public final class MoveInDirection implements TurnStep {
    final private Entity<?> entity;
    final private Offset direction;

    /**
     * Creates a new MoveTowardsPlayer action for the given entity.
     *
     * @param entity The entity that will move towards the player.
     */
    public MoveInDirection(Entity<?> entity, Offset direction) {
        this.entity = entity;
        this.direction = direction;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        return entity.moveBy(direction);
    }
}
