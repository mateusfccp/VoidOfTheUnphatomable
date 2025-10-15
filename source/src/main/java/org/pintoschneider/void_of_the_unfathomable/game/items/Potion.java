package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;

public final class Potion extends Item {
    public Potion() {
        super("Potion", ItemType.CONSUMABLE);
    }

    void effect(Player player) {
        player.setCurrentHealth(player.currentHealth() + 10);
    }
}
