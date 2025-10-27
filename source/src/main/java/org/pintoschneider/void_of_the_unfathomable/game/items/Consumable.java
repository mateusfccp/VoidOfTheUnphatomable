package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;

/**
 * A class representing consumable items that can be used by the player to produce various effects.
 */
public abstract class Consumable extends Item {
    protected Consumable(String name, String description) {
        super(name, description);
    }

    public abstract void onConsume(Player player);
}

