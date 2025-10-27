package org.pintoschneider.void_of_the_unfathomable.game.items.key_items;

import org.pintoschneider.void_of_the_unfathomable.game.items.Item;

/**
 * A key item representing a Fragment of Nothingness in the game.
 * <p>
 * A Fragment of Nothingness is a shard that pulsates with void energy, absorbing light and warmth around it. It is
 * dropped by all enemies, and can be used as a currency to trade with The Mender of Minds.
 */
public final class FragmentOfNothingness extends Item {
    /**
     * Constructs a new Fragment of Nothingness item with its name and description.
     */
    public FragmentOfNothingness() {
        super(
            "Fragment of Nothingness",
            "A small shard pulsating with an eerie void energy. It seems to absorb light and warmth around it."
        );
    }
}