package org.pintoschneider.void_of_the_unfathomable.game.enemies;

import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.List;
import java.util.Objects;

/**
 * A class representing an enemy in the game.
 */
public abstract class Enemy {
    private final String name;
    private final int health;
    private final int maxHealth;
    private final int attack;
    private final int defense;

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

    public abstract List<TurnStep> processTurn(Map.Entity<Enemy> entity);
}

