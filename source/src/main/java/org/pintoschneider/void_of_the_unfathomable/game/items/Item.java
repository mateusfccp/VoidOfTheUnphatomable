package org.pintoschneider.void_of_the_unfathomable.game.items;

/**
 * A sealed class representing a generic item in the game.
 * <p>
 * This class serves as a base for specific types of items, such as consumables and equippables.
 */
public interface Item {
    /**
     * Gets the name of the item.
     *
     * @return The name of the item
     */
    public String name();

    /**
     * Gets the description of the item.
     *
     * @return The description of the item
     */
    public String description();
}

