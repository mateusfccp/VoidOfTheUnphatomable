package org.pintoschneider.void_of_the_unfathomable.game.items.consumables;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.items.Consumable;

/**
 * A class representing a bottle of fluoxetine.
 * <p>
 * When consumed, it removes the {@code StatusEffect.DEPRESSION} status effect from the player.
 * <p>
 * When used repeatedly, it may cause negative side effects.
 */
public final class FluoxetineBottle implements Consumable {
    @Override
    public String name() {
        return "Frasco de Fluoxetina";
    }

    @Override
    public String description() {
        return "Pequeñas cápsulas que prometen devolver el color al mundo. Pero una vez que el cerebro prueba esta calma artificial, no le gusta que se la quiten.";
    }

    @Override
    public void onConsume(Player player) {
        player.removeStatusEffect(StatusEffect.DEPRESSION);
        player.takeFluoxetineDose();
    }
}
