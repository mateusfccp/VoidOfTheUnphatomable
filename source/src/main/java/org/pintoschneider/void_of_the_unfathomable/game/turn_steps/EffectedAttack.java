package org.pintoschneider.void_of_the_unfathomable.game.turn_steps;

import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.Enemy;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;
import org.pintoschneider.void_of_the_unfathomable.game.entities.PlayerEntity;

import java.util.Random;

/**
 * A {@link TurnStep} that performs a regular attack by an entity on the player.
 * <p>
 * A regular attack will consider the entity's attack power and the target's defense to calculate damage.
 *
 * @param <T> The type of enemy associated with the entity performing the attack.
 */
public final class EffectedAttack<T extends Enemy> implements TurnStep {
    final Entity<T> entity;
    final StatusEffect effect;
    final double effectChance;
    final Random random = new Random();

    /**
     * Creates a new EffectedAttack action for the given entity.
     *
     * @param entity       The entity that will attack the player.
     * @param effect       The status effect to apply on hit.
     * @param effectChance The chance to apply the status effect (between 0.0 and 1.0).
     */
    public EffectedAttack(Entity<T> entity, StatusEffect effect, double effectChance) {
        this.entity = entity;
        this.effect = effect;
        this.effectChance = effectChance;
    }

    @Override
    public boolean execute(Boolean lastTurnResult) {
        final PlayerEntity playerEntity = entity.map().getEntitiesOfType(PlayerEntity.class).getFirst();

        assert entity.distanceTo(playerEntity) == 1 : "Entity must be adjacent to the player to perform a regular attack.";

        final Enemy enemy = entity.associatedObject();
        playerEntity.damage(enemy.attack());

        if (random.nextDouble() < effectChance) {
            playerEntity.associatedObject().applyStatusEffect(effect);
        }

        return true;
    }
}
