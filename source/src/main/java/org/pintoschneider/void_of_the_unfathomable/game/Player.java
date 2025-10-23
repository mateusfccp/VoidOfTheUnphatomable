package org.pintoschneider.void_of_the_unfathomable.game;

import org.pintoschneider.void_of_the_unfathomable.game.items.Consumable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * A class representing the player character in the game including their attributes, status effects, and inventory.
 */
public final class Player {
    private int currentHealth = maximumHealth() / 2;
    private int currentColorPoints = maximumColorPoints();
    private int attackPower;
    private int defensePower;
    private int creativityPower;
    private int neuralToxicity = 0;
    private int fluoxetineDoses = 0;
    private int turnsWithoutFluoxetine = 0;
    private final EnumSet<StatusEffect> statusEffects = EnumSet.noneOf(StatusEffect.class);
    private final ArrayList<Item> inventory = new ArrayList<>();

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
     * Gets the current color points of the player.
     *
     * @return The current color points of the player.
     */
    public int getCurrentColorPoints() {
        return currentColorPoints;
    }

    /**
     * Gets the maximum color points of the player.
     *
     * @return The maximum color points of the player.
     */
    public int maximumColorPoints() {
        return 10;
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
     * Increases the player's neural toxicity by a specified amount.
     *
     * @param amount The amount to increase neural toxicity by.
     */
    public void increaseNeuralToxicityBy(int amount) {
        neuralToxicity = neuralToxicity + amount;
    }

    /**
     * Increases the player's fluoxetine doses and resets the turns without fluoxetine counter.
     */
    public void takeFluoxetineDose() {
        fluoxetineDoses = fluoxetineDoses + 1;
        turnsWithoutFluoxetine = 0;
    }

    /**
     * Gets the current status effects affecting the player.
     *
     * @return An {@link EnumSet} of {@link StatusEffect} representing the player's current status effects
     */
    public EnumSet<StatusEffect> statusEffects() {
        final EnumSet<StatusEffect> statusEffects = this.statusEffects.clone();

        if (currentHealth == 0) {
            statusEffects.add(StatusEffect.DEATH);
        }

        if (currentHealth < maximumHealth() * 0.3) {
            statusEffects.add(StatusEffect.INSANITY);
        }

        if (neuralToxicity >= 5) {
            statusEffects.add(StatusEffect.TARDIVE_DYSKINESIA);
        } else if (neuralToxicity >= 3) {
            statusEffects.add(StatusEffect.DYSKINESIA);
        }

        if (fluoxetineDoses > 0) {
            statusEffects.add(StatusEffect.DEPENDENCY);
        }

        if (turnsWithoutFluoxetine >= 100 && statusEffects.contains(StatusEffect.DEPENDENCY)) {
            statusEffects.add(StatusEffect.DISCONTINUATION_SYNDROME);
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

    /**
     * Adds an item to the player's inventory.
     *
     * @param item The item to add to the inventory.
     */
    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    /**
     * Sorts player's inventory alphabetically.
     *
     * @param item The item to add to the inventory.
     */
    public void sortInventory(Item item) {
    	int index = binarySearch(this.inventory, item.name());
    	this.inventory.set(index, item);
    }
    
    /**
     * Searches the player's inventory with an efficiency of O(log n). Returns the index of where to place the item in the ArrayList
     *
     * @param list the player's inventory, name the name of the item to be added.
     */
    public int binarySearch(ArrayList<Item> list, String name) {
        int low = 0;
        int high = list.size();
        int mid = -1;

        while (low <= high) {
            mid = low + (high - low) / 2;

            if (list.get(mid).name().equals(name)) {
                return mid;
            }
            else if (list.get(mid).name().compareToIgnoreCase(name) < 0) {
                low = mid + 1;
            }      else if (list.get(mid).name().compareToIgnoreCase(name) > 0) {
                high = mid - 1;
            }
        }
        return -1;
    }
    
    
    /**
     * Uses a consumable item from the player's inventory.
     *
     * @param item The consumable item to use.
     */
    public void useItem(Consumable item) {
        if (inventory.contains(item)) {
            item.onConsume(this);
            inventory.remove(item);
        }
    }
}
