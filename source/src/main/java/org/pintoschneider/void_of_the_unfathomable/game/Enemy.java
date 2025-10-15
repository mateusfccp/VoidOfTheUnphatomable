package org.pintoschneider.void_of_the_unfathomable.game;

public class Enemy {
    private final String name;
    private final int health;
    private final int attack;
    private final int defense;

    public Enemy(String name, int health, int attack, int defense) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    static final Enemy[] enemies = new Enemy[]{
        new Enemy("Goblin", 30, 5, 2),
        new Enemy("Orc", 50, 10, 5),
        new Enemy("Troll", 80, 15, 10),
        new Enemy("Dragon", 200, 25, 20)
    };
}
