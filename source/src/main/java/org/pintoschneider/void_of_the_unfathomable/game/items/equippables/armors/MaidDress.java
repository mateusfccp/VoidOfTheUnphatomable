package org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors;

import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;

/**
 * The Maid Dress armor item.
 */
public class MaidDress implements Equippable {
    @Override
    public EquippableSlot slot() {
        return EquippableSlot.ARMOR;
    }

    @Override
    public String name() {
        return "Vestido de sirvienta";
    }

    @Override
    public String description() {
        return "¡UwU!\nUn vestido de sirvienta súper lindo que te hace sentir adorable y servicial al mismo tiempo.";
    }
}
