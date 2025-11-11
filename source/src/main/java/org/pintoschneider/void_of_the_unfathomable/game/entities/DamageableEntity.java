package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

/**
 * An entity that can be damaged.
 */
public abstract class DamageableEntity<T> extends Entity<T> {
    protected DamageableEntity(Offset position, T associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    /**
     * Inflicts damage to the entity.
     *
     * @param amount The amount of damage to inflict.
     */
    abstract public void damage(int amount);
}
