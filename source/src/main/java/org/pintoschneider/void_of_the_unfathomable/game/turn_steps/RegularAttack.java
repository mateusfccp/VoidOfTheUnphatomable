package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.Enemy;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

/**
 * A {@link TurnStep} that performs a regular attack by an entity on the player.
 * <p>
 * A regular attack will consider the entity's attack power and the target's defense to calculate damage.
 */
public final class RegularAttack implements TurnStep {
    final Map.Entity<Enemy> entity;

    /**
     * Creates a new MoveTowardsPlayer action for the given entity.
     *
     * @param entity The entity that will move towards the player.
     */
    public RegularAttack(Map.Entity<Enemy> entity) {
        this.entity = entity;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        final Map.Entity<Player> playerEntity = entity.map().entitiesOfType(Player.class).getFirst();

        assert entity.distanceTo(playerEntity) == 1 : "Entity must be adjacent to the player to perform a regular attack.";

        final Player player = playerEntity.associatedObject();
        final Enemy enemy = entity.associatedObject();
        player.damage(enemy.attack()); // TODO(mateusfccp): Consider player defense

        return true;
    }
}
