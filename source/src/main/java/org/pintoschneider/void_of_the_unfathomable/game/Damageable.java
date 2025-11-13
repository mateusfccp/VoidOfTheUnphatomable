package org.pintoschneider.void_of_the_unfathomable.game;

/**
 * An interface that represents an entity that can take damage.
 */
public interface Damageable {
    /**
     * Inflicts damage to the player, reducing their current health.
     * <p>
     * Returns weather the entity should be destroyed (health <= 0) after the damage is applied.
     *
     * @param damage The amount of damage to inflict
     * @return True if the entity's health is less than or equal to zero after taking damage, false otherwise.
     */
    boolean damage(int damage);
}
