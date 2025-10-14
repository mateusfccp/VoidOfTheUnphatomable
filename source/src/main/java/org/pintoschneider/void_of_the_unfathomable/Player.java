package org.pintoschneider.void_of_the_unfathomable;

import java.util.EnumSet;

public class Player {
    // Health

    /**
     * Gets the current health of the player.
     *
     * @return The current health of the player
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    private int currentHealth;

    /**
     * Gets the maximum health of the player.
     *
     * @return The maximum health of the player
     */
    public int getMaximumHealth() {
        return maximumHealth;
    }

    private int maximumHealth;

    // Attributes
    private int attackPower;
    private int defensePower;
    private int creativityPower;

    // Status Effects

    /**
     * Gets the current status effects affecting the player.
     *
     * @return An {@link EnumSet} of {@link StatusEffect} representing the player's current status effects
     */
    public EnumSet<StatusEffect> getStatusEffects() {
        return statusEffects.clone();
    }

    private EnumSet<StatusEffect> statusEffects;

}
