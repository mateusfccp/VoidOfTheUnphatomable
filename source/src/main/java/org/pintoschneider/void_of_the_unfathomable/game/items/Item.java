package org.pintoschneider.void_of_the_unfathomable.game.items;

import java.util.Objects;

/**
 * A sealed class representing a generic item in the game.
 * <p>
 * This class serves as a base for specific types of items, such as consumables and equippables.
 */
public sealed class Item permits Consumable, Equippable {
    private final String name;
    private final String description;

    protected Item(String name, String description) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
    }

    /**
     * Gets the name of the item.
     *
     * @return The name of the item
     */
    public String name() {
        return name;
    }

    /**
     * Gets the description of the item.
     *
     * @return The description of the item
     */
    public String description() {
        return description;
    }
}

