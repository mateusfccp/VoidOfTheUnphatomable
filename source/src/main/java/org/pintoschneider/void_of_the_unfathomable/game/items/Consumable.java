package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;

import java.util.List;

public abstract non-sealed class Consumable extends Item {
    public Consumable(String name, String description) {
        super(name, description);
    }

    public abstract void onConsume(Player player);


    private static final Consumable[] consumables = {
        new Consumable(
            "Ampolla de Haloperidol",
            "Un neuroléptico potente en una ampolla de vidrio. Detiene las pesadillas en el mundo real de forma casi instantánea. El silencio que deja puede ser tan aterrador como el ruido que se lleva."
        ) {
            @Override
            public void onConsume(Player player) {
                player.heal(20);
                player.increaseNeuralToxicityBy(1);
            }
        },

        new Consumable(
            "Frasco de Fluoxetina",
            "Pequeñas cápsulas que prometen devolver el color al mundo. Pero una vez que el cerebro prueba esta calma artificial, no le gusta que se la quiten"
        ) {
            @Override
            public void onConsume(Player player) {
                player.removeStatusEffect(StatusEffect.DEPRESSION);
                player.takeFluoxetineDose();
            }
        }
    };

    public static List<Consumable> all() {
        return List.of(consumables);
    }
}
