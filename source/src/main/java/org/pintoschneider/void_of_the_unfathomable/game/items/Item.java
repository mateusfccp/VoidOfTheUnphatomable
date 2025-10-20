package org.pintoschneider.void_of_the_unfathomable.game.items;

public sealed class Item permits Consumable, Equippable {
    private final String name;
    private final String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

