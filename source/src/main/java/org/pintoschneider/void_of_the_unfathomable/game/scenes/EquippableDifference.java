package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Row;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Composent;
import org.pintoschneider.void_of_the_unfathomable.ui.core.MainAxisSize;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.function.Function;

/**
 * A UI component that displays the difference in a player's stat
 * if a selected Equippable item were to be equipped or unequipped.
 */
public class EquippableDifference extends Composent {
    final Player player;
    final Function<Player, Integer> playerAttributeGetter;
    final Item item;
    final Function<Equippable, Integer> equippableModifierGetter;

    /**
     * Constructs an EquippableDifference component.
     *
     * @param player                   The player to check stats against.
     * @param playerAttributeGetter    A function to get the base stat from the player (e.g., Player::attack).
     * @param item                     The selected item (which may or may not be an Equippable).
     * @param equippableModifierGetter A function to get the relevant modifier from the Equippable (e.g., Equippable::attackModifier).
     */
    EquippableDifference(
        Player player,
        Function<Player, Integer> playerAttributeGetter,
        Item item,
        Function<Equippable, Integer> equippableModifierGetter
    ) {
        this.player = player;
        this.item = item;
        this.equippableModifierGetter = equippableModifierGetter;
        this.playerAttributeGetter = playerAttributeGetter;
    }

    @Override
    public Component build() {
        final int originalValue = playerAttributeGetter.apply(player);

        if (!(item instanceof Equippable equippable)) {
            return new Text("%d".formatted(originalValue), Paint.DIM);
        }

        final boolean isEquipped = player.equippedItem(equippable.slot()) == equippable;

        final int newValue = originalValue + equippableModifierGetter.apply(equippable) * (isEquipped ? -1 : 1);

        final boolean hasDifference = originalValue != newValue;
        final Paint oldValuePaint = hasDifference ? null : Paint.DIM;
        final Paint newValuePaint;

        if (newValue > originalValue) {
            newValuePaint = new Paint().withForegroundColor(ColorPalette.FOREST_GREEN);
        } else if (newValue < originalValue) {
            newValuePaint = new Paint().withForegroundColor(ColorPalette.SCARLET);
        } else {
            newValuePaint = Paint.DIM;
        }

        return new Row(
            new Text("%d".formatted(originalValue), oldValuePaint),
            new Text(" → ", Paint.DIM),
            new Text("%d".formatted(newValue), newValuePaint)
        ).mainAxisSize(MainAxisSize.MIN);
    }
}
