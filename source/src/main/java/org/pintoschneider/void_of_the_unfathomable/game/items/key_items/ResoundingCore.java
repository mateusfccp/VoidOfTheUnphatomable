package org.pintoschneider.void_of_the_unfathomable.game.items.key_items;

import org.pintoschneider.void_of_the_unfathomable.game.items.Item;

/**
 * A key item representing the Resounding Core in the game.
 * <p>
 * The Resounding Core is a mysterious artifact that emits a deep, resonant hum. It is said to hold the power to stop
 * the encroaching void and restore balance to the world.
 */
public final class ResoundingCore implements Item {
    @Override
    public String name() {
        return "Núcleo Resonante";
    }

    @Override
    public String description() {
        return "Un artefacto misterioso que emite un zumbido profundo y resonante. Se dice que posee el poder de detener el avance del vacío y restaurar el equilibrio en el mundo.";
    }
}