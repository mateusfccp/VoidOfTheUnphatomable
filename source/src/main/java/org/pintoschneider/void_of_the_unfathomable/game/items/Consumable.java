package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;

/**
 * A class representing consumable items that can be used by the player to produce various effects.
 */
public interface Consumable extends Item {
    public abstract void onConsume(Player player);
}

