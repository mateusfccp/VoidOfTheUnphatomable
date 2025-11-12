package org.pintoschneider.void_of_the_unfathomable.game.items.consumables;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.items.Consumable;

/**
 * A class representing a Haloperidol Ampoule consumable item.
 * <p>
 * When consumed, it heals the player and increases neural toxicity.
 */
public final class HaloperidolAmpoule extends Consumable {
    @Override
    public String name() {
        return "Ampolla de Haloperidol";
    }

    @Override
    public String description() {
        return "Un neuroléptico potente en una ampolla de vidrio. Detiene las pesadillas en el mundo real de forma casi instantánea. El silencio que deja puede ser tan aterrador como el ruido que se lleva.";
    }

    @Override
    protected void onConsume(Player player) {
        player.heal(20);
        player.increaseNeuralToxicityBy(1);
    }

    @Override
    public int price() {
        return 5;
    }
}
