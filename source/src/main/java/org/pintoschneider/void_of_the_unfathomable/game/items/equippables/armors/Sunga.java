package org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors;

import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;

/**
 * The Sunga armor item.
 * <p>
 * It is a minimalistic swimwear that offers no protection but great freedom of movement, and increases the player's
 * aura.
 */
public class Sunga implements Equippable {
    @Override
    public EquippableSlot slot() {
        return EquippableSlot.ARMOR;
    }

    @Override
    public String name() {
        return "Sunga";
    }

    @Override
    public String description() {
        return "No te protege de nada, pero te da una libertad de movimiento increíble, y hace con que los enemigos se sientan intimidados con tamaña aura.";
    }

    @Override
    public int price() {
        return 30;
    }
}
