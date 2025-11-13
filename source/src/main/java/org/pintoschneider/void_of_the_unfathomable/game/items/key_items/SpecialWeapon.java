package org.pintoschneider.void_of_the_unfathomable.game.items.key_items;

import org.pintoschneider.void_of_the_unfathomable.game.entities.PlayerEntity;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;

/**
 * An abstract class that represents a special weapon that can be equipped by the player.
 */
public abstract class SpecialWeapon implements Equippable {
    @Override
    final public EquippableSlot slot() {
        return EquippableSlot.WEAPON;
    }

    /**
     * Activates the special ability of the weapon.
     *
     * @param player The player entity using the special ability.
     */
    abstract public void specialAbility(PlayerEntity player);
}
