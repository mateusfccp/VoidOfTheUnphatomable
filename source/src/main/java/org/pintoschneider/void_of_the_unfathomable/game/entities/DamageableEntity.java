package org.pintoschneider.void_of_the_unfathomable.game.entities;

/**
 * An interface representing an entity that can take damage.
 */
public interface DamageableEntity {
    /**
     * Inflicts damage to the entity.
     *
     * @param amount The amount of damage to inflict.
     */
    void damage(int amount);
}
