package org.pintoschneider.void_of_the_unfathomable.game;

import java.util.Objects;

public class Enemy {
    private final String name;
    private final int health;
    private final int attack;
    private final int defense;

    public Enemy(String name, int health, int attack, int defense) {
        this.name = Objects.requireNonNull(name);
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public String name() {
        return name;
    }

    public int health() {
        return health;
    }

    public int attack() {
        return attack;
    }

    public int defense() {
        return defense;
    }

    static final Enemy[] enemies = new Enemy[]{
        new Enemy("Goblin", 30, 5, 2),
        new Enemy("Orc", 50, 10, 5),
        new Enemy("Troll", 80, 15, 10),
        new Enemy("Dragon", 200, 25, 20)
    };
}
