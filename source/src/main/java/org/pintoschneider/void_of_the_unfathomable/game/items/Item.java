package org.pintoschneider.void_of_the_unfathomable.game.items;

/**
 * A sealed class that represents a generic item in the game.
 * <p>
 * This class serves as a base for specific types of items, such as consumables and equippables.
 */
public interface Item {
    /**
     * Gets the name of the item.
     *
     * @return The name of the item
     */
    String name();

    /**
     * Gets the description of the item.
     *
     * @return The description of the item
     */
    String description();

    /**
     * Gets the price of the item, denominated in "Fragments of Nothingness".
     * By default, an item has no value.
     *
     * @return The price of the item.
     */
    default int price() {
        return 0;
    }
}
