package org.pintoschneider.void_of_the_unfathomable.game.items.key_items;

import org.pintoschneider.void_of_the_unfathomable.game.enemies.Abyssmonkey;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;

/**
 * A key item representing a family photo.
 * <p>
 * The family photo is what prevents the player from going insane just by being in the void.
 * <p>
 * When the {@link Abyssmonkey} steals it, the player starts losing sanity over time.
 */
public class FamilyPhoto implements Item {
    @Override
    public String name() {
        return "Foto de Família";
    }

    @Override
    public String description() {
        return "Una foto desgastada de una familia sonriente, ahora desvanecida y borrosa. A pesar de la oscuridad que te rodea, esta imagen evoca un sentimiento de calidez y pertenencia, recordándote los lazos que una vez tuviste. En la foto, no estás presente, lo que sugiere que no sea tu família, pero no podés acordarte, entonces no te importa.";
    }
}
