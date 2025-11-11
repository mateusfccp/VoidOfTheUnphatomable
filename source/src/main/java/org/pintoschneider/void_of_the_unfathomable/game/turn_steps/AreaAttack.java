package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.enemies.Enemy;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;
import org.pintoschneider.void_of_the_unfathomable.game.entities.PlayerEntity;

/**
 * A {@link TurnStep} that performs a regular attack by an entity on the player.
 * <p>
 * A regular attack will consider the entity's attack power and the target's defense to calculate damage.
 */
public final class AreaAttack<T extends Enemy> implements TurnStep {
    final int area;
    final Entity<T> entity;

    /**
     * Creates a new RegularAttack action for the given entity.
     *
     * @param entity The entity that will attack the player.
     */
    public AreaAttack(Entity<T> entity, int area) {
        this.entity = entity;
        this.area = area;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        final PlayerEntity playerEntity = entity.map().getEntitiesOfType(PlayerEntity.class).getFirst();

        if (playerEntity.distanceTo(entity) <= area) {
            final Enemy enemy = entity.associatedObject();
            playerEntity.damage(enemy.attack());
        }

        return true;
    }
}
