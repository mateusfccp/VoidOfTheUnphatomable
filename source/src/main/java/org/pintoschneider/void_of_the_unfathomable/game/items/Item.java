package org.pintoschneider.void_of_the_unfathomable.game.items;

import org.pintoschneider.void_of_the_unfathomable.game.Player;

public class Item {
    private final String name;
    private final ItemType type;

    public Item(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    void effect(Player player) {
    }
}

enum ItemType {
    CONSUMABLE,
    ARMOR,
    WEAPON;
}