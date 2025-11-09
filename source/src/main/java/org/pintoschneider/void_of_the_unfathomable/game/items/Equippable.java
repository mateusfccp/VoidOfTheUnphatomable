package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.EnumSet;

/**
 * A class representing equippable items that can be worn or used by the player.
 */
public interface Equippable extends Item {
    /**
     * Gets the slot where this equippable item can be equipped.
     *
     * @return The equippable slot.
     */
    EquippableSlot slot();

    /**
     * Gets the attack modifier provided by this equippable item.
     *
     * @return The attack modifier.
     */
    default int attackModifier() {
        return 0;
    }

    /**
     * Gets the defense modifier provided by this equippable item.
     *
     * @return The defense modifier.
     */
    default int defenseModifier(Player player) {
        return 0;
    }

    /**
     * Gets the creativity modifier provided by this equippable item.
     *
     * @return The creativity modifier.
     */
    default int creativityModifier() {
        return 0;
    }

    /**
     * Modifies the player's paint when this equippable item is worn.
     *
     * @param paint The original paint.
     * @return The modified paint.
     */
    default Paint playerPaint(Paint paint) {
        return paint;
    }

    default void transformStatusEffects(EnumSet<StatusEffect> originalStatusEffects) {}
}

