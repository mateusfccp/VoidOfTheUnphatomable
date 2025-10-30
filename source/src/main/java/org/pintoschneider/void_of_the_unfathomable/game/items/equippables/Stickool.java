package org.pintoschneider.void_of_the_unfathomable.game.items.equippables;

import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;

/**
 * A simple stick that can be equipped as a weapon.
 */
public class Stickool implements Equippable {
    @Override
    public EquippableSlot slot() {
        return EquippableSlot.WEAPON;
    }

    @Override
    public String name() {
        return "Pialo";
    }

    @Override
    public String description() {
        return "Un palo que está re piola. Lo levaste del piso y se siente bastante bien en la mano.";
    }

    @Override
    public int attackModifier() {
        return 1;
    }
}
