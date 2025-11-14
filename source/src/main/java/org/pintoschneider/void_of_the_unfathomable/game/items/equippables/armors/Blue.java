package org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors;

import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

/**
 * Blue.
 * <p>
 * Makes the player blue. While blue, defense is increased by 3 instead of 1 if the player has
 * {@link StatusEffect.DEPRESSION}.
 */
public class Blue implements Equippable {
    @Override
    public EquippableSlot slot() {
        return EquippableSlot.ARMOR;
    }

    @Override
    public String name() {
        return "Blue";
    }

    @Override
    public String description() {
        return "Jessie, why are you blue?";
    }

    @Override
    public Paint playerPaint(Paint paint) {
        return paint.withForegroundColor(
            ColorPalette.POWDER_BLUE
        );
    }

    @Override
    public int defenseModifier(Player player) {
        if (player.statusEffects().contains(StatusEffect.DEPRESSION)) {
            return 3;
        } else {
            return 1;
        }
    }
}
