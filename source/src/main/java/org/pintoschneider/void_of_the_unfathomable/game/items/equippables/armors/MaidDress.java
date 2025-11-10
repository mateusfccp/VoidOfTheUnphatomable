package org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors;

import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

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
        return "Vestido de Maid";
    }

    @Override
    public String description() {
        return "UwU :3";
    }

    @Override
    public Paint playerPaint(Paint paint) {
        return paint.withForegroundColor(ColorPalette.BLUSH).withBlink(true);
    }
}
