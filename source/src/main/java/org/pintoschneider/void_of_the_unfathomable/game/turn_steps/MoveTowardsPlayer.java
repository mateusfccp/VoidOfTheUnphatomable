package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

/**
 * A {@link TurnStep} that moves an entity towards the player.
 */
public final class MoveTowardsPlayer implements TurnStep {
    final Map.Entity<?> entity;

    /**
     * Creates a new MoveTowardsPlayer action for the given entity.
     *
     * @param entity The entity that will move towards the player.
     */
    public MoveTowardsPlayer(Map.Entity<?> entity) {
        this.entity = entity;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        final Map.Entity<Player> player = entity.map().entitiesOfType(Player.class).getFirst();

        return entity.moveTowards(player.position());
    }
}
