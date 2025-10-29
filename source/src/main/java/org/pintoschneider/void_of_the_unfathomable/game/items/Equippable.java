package org.pintoschneider.void_of_the_unfathomable.game.items;

/**
 * A class representing equippable items that can be worn or used by the player.
 */
public interface Equippable extends Item {
    /**
     * Gets the slot where this equippable item can be equipped.
     *
     * @return The equippable slot.
     */
    EquippableSlot slot();

    /**
     * Gets the attack modifier provided by this equippable item.
     *
     * @return The attack modifier.
     */
    default int attackModifier() {
        return 0;
    }

    /**
     * Gets the defense modifier provided by this equippable item.
     *
     * @return The defense modifier.
     */
    default int defenseModifier() {
        return 0;
    }

    /**
     * Gets the creativity modifier provided by this equippable item.
     *
     * @return The creativity modifier.
     */
    default int creativityModifier() {
        return 0;
    }
}

