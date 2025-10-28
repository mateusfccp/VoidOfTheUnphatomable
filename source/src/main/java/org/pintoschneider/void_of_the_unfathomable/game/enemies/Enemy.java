package org.pintoschneider.void_of_the_unfathomable.game.enemies;

import java.util.Objects;

/**
 * A class representing an enemy in the game.
 */
public abstract class Enemy {
    private final String name;
    private final int maxHealth;
    private final int attack;
    private final int defense;
    private int health;

    protected Enemy(String name, int maxHealth, int attack, int defense) {
        this.name = Objects.requireNonNull(name);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.attack = attack;
        this.defense = defense;
    }

    /**
     * Gets the name of the enemy.
     *
     * @return The name of the enemy.
     */
    public String name() {
        return name;
    }

    /**
     * Gets the health of the enemy.
     *
     * @return The health of the enemy.
     */
    public int health() {
        return health;
    }

    /**
     * Gets the maximum health of the enemy.
     *
     * @return The maximum health of the enemy.
     */
    public int maxHealth() {
        return maxHealth;
    }

    /**
     * Gets the attack power of the enemy.
     *
     * @return The attack power of the enemy.
     */
    public int attack() {
        return attack;
    }

    /**
     * Gets the defense power of the enemy.
     *
     * @return The defense power of the enemy.
     */
    public int defense() {
        return defense;
    }

    /**
     * Damages the enemy by the specified amount.
     *
     * @param amount The amount of damage to deal to the enemy.
     */
    public void damage(int amount) {
        assert amount >= 0 : "Damage amount must be non-negative";

        final int defendedAmount = Math.max(1, amount - defense);

        health = Math.max(0, health - defendedAmount);
    }
}

