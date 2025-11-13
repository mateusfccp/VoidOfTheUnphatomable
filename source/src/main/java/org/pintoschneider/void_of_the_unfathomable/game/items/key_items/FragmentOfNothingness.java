package org.pintoschneider.void_of_the_unfathomable.game.items.key_items;

import org.pintoschneider.void_of_the_unfathomable.game.items.Item;

/**
 * A key item that represents a Fragment of Nothingness in the game.
 * <p>
 * A Fragment of Nothingness is a shard that pulsates with void energy, absorbing light and warmth around it. It is
 * dropped by all enemies, and can be used as a currency to trade with The Mender of Minds.
 */
public final class FragmentOfNothingness implements Item {
    @Override
    public String name() {
        return "Fragmento de Nulidad";
    }

    @Override
    public String description() {
        return "Un pequeño fragmento que pulsa con una horripilante energía nula. Parece absorber la luz y el calor a su alrededor.";
    }

    /**
     * The price of the currency item is 1, as it is the base unit of value.
     */
    @Override
    public int price() {
        return 1;
    }
}