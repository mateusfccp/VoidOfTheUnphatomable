package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;
import org.pintoschneider.void_of_the_unfathomable.game.entities.PlayerEntity;

/**
 * A {@link TurnStep} that moves an entity towards the player.
 */
public final class MoveTowardsPlayer implements TurnStep {
    private final Entity<?> entity;

    /**
     * Creates a new MoveTowardsPlayer action for the given entity.
     *
     * @param entity The entity that will move towards the player.
     */
    public MoveTowardsPlayer(Entity<?> entity) {
        this.entity = entity;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        final Entity<Player> player = entity.map().entitiesOfType(PlayerEntity.class).getFirst();

        return entity.moveTowards(player.position());
    }
}
