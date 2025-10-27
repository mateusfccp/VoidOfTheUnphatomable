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
    private static final int maximumHealth = 100;
    private static final int baseAttackPower = 5;
    private static final int baseDefensePower = 1;
    private static final int baseCreativityPower = 1;

    private final EnumSet<StatusEffect> statusEffects = EnumSet.noneOf(StatusEffect.class);
    private final ArrayList<Item> inventory = new ArrayList<>();
    private final int currentColorPoints = maximumColorPoints();

    private int currentHealth = maximumHealth() / 2;
    private int neuralToxicity = 0;
    private int fluoxetineDoses = 0;
    private int turnsWithoutFluoxetine = 0;

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
        return maximumHealth;
    }

    /**
     * Gets the current color points of the player.
     *
     * @return The current color points of the player.
     */
    public int currentColorPoints() {
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
     * Gets the player's attack power.
     *
     * @return The player's attack power.
     */
    public int attackPower() {
        return baseAttackPower;
    }

    /**
     * Gets the player's defense power.
     *
     * @return The player's defense power.
     */
    public int defensePower() {
        return baseDefensePower;
    }

    /**
     * Gets the player's creativity power.
     *
     * @return The player's creativity power.
     */
    public int creativityPower() {
        return baseCreativityPower;
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
        final int defendedDamage = Math.max(1, damage - defensePower());
        setCurrentHealth(currentHealth - defendedDamage);
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
     * Gets the player's inventory.
     *
     * @return A list of items in the player's inventory.
     */
    public List<Item> inventory() {
        return inventory;
    }

    /**
     * Adds an item to the player's inventory.
     * Sorts the inventory alphabetically  as the items get added
     *
     * @param item The item to add to the inventory.
     */
    public void addItemToInventory(Item item) {
        int index = binarySearch(this.inventory, item.name());
        if (index == inventory.size()) {
            this.inventory.add(item);
        } else {
            this.inventory.add(index, item);
        }
    }

    /**
     * Searches the player's inventory with an efficiency of O(log n).
     *
     * @param list The player's inventory,
     * @param name The name of the item to be added.
     * @return The index of where to place the item in the ArrayList.
     */
    private int binarySearch(ArrayList<Item> list, String name) {
        if (list.isEmpty()) return 0;

        int low = 0;
        int high = list.size();
        int mid = low + (high - low) / 2;

        while (low <= high) {
            mid = low + (high - low) / 2;

            if (list.get(mid).name().equals(name)) {
                return mid;
            } else if (list.get(mid).name().compareToIgnoreCase(name) < 0) {
                low = mid + 1;
            } else if (list.get(mid).name().compareToIgnoreCase(name) > 0) {
                high = mid - 1;
            }
        }

        return mid;
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
