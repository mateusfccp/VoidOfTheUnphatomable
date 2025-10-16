package org.pintoschneider.void_of_the_unfathomable.game;

import java.util.EnumSet;

public class Player {
    private int currentHealth;
    private int currentColorPoints;
    private int attackPower;
    private int defensePower;
    private int creativityPower;
    private EnumSet<StatusEffect> statusEffects;

    /**
     * Gets the current health of the player.
     *
     * @return The current health of the player
     */
    public int currentHealth() {
        return currentHealth;
    }

    /**
     * Gets the maximum health of the player.
     *
     * @return The maximum health of the player
     */
    public int maximumHealth() {
        return 100;
    }

    /**
     * Sets the current health of the player and updates status effects accordingly.
     *
     * @param health The new current health of the player
     */
    public void setCurrentHealth(int health) {
        this.currentHealth = Math.clamp(health, 0, maximumHealth());
    }

    /**
     * Heals the player by a specified amount.
     *
     * @param health The amount of health to restore
     */
    public void heal(int health) {
        setCurrentHealth(currentHealth + health);
    }

    /**
     * Inflicts damage to the player, reducing their current health.
     *
     * @param damage The amount of damage to inflict
     */
    public void damage(int damage) {
        setCurrentHealth(currentHealth - damage);
    }

    /**
     * Gets the current status effects affecting the player.
     *
     * @return An {@link EnumSet} of {@link StatusEffect} representing the player's current status effects
     */
    public EnumSet<StatusEffect> statusEffects() {
        final EnumSet<StatusEffect> statusEffects = this.statusEffects.clone();
        if (currentHealth == 0) {
            statusEffects.add(StatusEffect.DEAD);
        }

        if (currentHealth < maximumHealth() * 0.3) {
            statusEffects.add(StatusEffect.INSANE);
        }

        return statusEffects;
    }

    /**
     * Applies a status effect to the player.
     *
     * @param statusEffect The status effect to apply.
     */
    public void applyStatusEffect(StatusEffect statusEffect) {
        statusEffects.add(statusEffect);
    }

    /**
     * Removes a status effect from the player.
     *
     * @param statusEffect The status effect to remove.
     */
    public void removeStatusEffect(StatusEffect statusEffect) {
        statusEffects.remove(statusEffect);
    }

    /**
     * Clears all status effects from the player.
     */
    public void clearStatusEffects() {
        statusEffects.clear();
    }
}
