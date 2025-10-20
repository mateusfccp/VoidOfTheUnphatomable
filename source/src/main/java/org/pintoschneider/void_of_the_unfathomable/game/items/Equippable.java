package org.pintoschneider.void_of_the_unfathomable.game.items;

/**
 * A class representing equippable items that can be worn or used by the player.
 */
public abstract non-sealed class Equippable extends Item {
    final EquippableSlot slot;

    Equippable(String name, String description, EquippableSlot slot) {
        super(name, description);
        this.slot = slot;
    }
}

