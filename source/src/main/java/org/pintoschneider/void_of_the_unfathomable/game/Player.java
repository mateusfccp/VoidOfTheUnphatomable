package org.pintoschneider.void_of_the_unfathomable.game;

import java.util.EnumSet;

public class Player {
    private int currentHealth;
    private int currentColorPoints;
    private int maximumHealth;
    private int attackPower;
    private int defensePower;
    private int creativityPower;
    private EnumSet<StatusEffect> statusEffects;

    /**
     * Gets the current health of the player.
     *
     * @return The current health of the player
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets the maximum health of the player.
     *
     * @return The maximum health of the player
     */
    public int getMaximumHealth() {
        return maximumHealth;
    }

    public int setCurrentHealth(int health) {
        this.currentHealth = Math.min(health, maximumHealth);
        return currentHealth;
    }

    /**
     * Gets the current status effects affecting the player.
     *
     * @return An {@link EnumSet} of {@link StatusEffect} representing the player's current status effects
     */
    public EnumSet<StatusEffect> getStatusEffects() {
        return statusEffects.clone();
    }

    public void healStatusEffects() {
        statusEffects.clear();
    }
}
