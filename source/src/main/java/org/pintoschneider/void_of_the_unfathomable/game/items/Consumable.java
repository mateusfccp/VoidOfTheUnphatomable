package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;

import java.util.WeakHashMap;

/**
 * A class representing consumable items that can be used by the player to produce various effects.
 */
public abstract class Consumable implements Item {
    private static final WeakHashMap<Class<? extends Consumable>, Integer> useCount = new WeakHashMap<>();

    /**
     * Consumes the item, applying its effects to the player.
     *
     * @param player The player who consumed the item.
     */
    final public void consume(Player player) {
        useCount.putIfAbsent(this.getClass(), 0);
        useCount.put(this.getClass(), useCount.get(this.getClass()) + 1);
        onConsume(player);
    }

    abstract protected void onConsume(Player player);

    /**
     * Gets the number of times a specific consumable has been used.
     *
     * @param consumableClass The class of the consumable item.
     * @return The number of times the consumable has been used.
     */
    public static int getUseCount(Class<? extends Consumable> consumableClass) {
        return useCount.getOrDefault(consumableClass, 0);
    }
}

