package org.pintoschneider.void_of_the_unfathomable.game;

import org.pintoschneider.void_of_the_unfathomable.game.items.Consumable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.FluoxetineBottle;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.*;

/**
 * A class representing the player character in the game including their attributes, status effects, and inventory.
 */
public final class Player implements Damageable {
    private static final int maximumHealth = 100;
    private static final int baseAttack = 3;
    private static final int baseDefense = 0;

    private final EnumSet<StatusEffect> statusEffects = EnumSet.noneOf(StatusEffect.class);
    private final Map<EquippableSlot, Equippable> equippedItems = new EnumMap<>(EquippableSlot.class);
    private final List<Item> inventory = new ArrayList<>();

    private int currentHealth = maximumHealth();
    private int neuralToxicity = 0;
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
     * Gets the player's attack power.
     *
     * @return The player's attack power.
     */
    public int attack() {
        return baseAttack + equippedItems.values().stream()
            .mapToInt(Equippable::attackModifier)
            .sum();
    }

    /**
     * Gets the player's defense power.
     *
     * @return The player's defense power.
     */
    public int defense() {
        return baseDefense + equippedItems.values().stream()
            .mapToInt(e -> e.defenseModifier(this))
            .sum();
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

    @Override
    public boolean damage(int damage) {
        final int defendedDamage = Math.max(1, damage - defense());
        setCurrentHealth(currentHealth - defendedDamage);

        return currentHealth == 0;
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
        turnsWithoutFluoxetine = 0;
    }

    /**
     * Increments the number of turns the player has gone without taking fluoxetine.
     */
    public void incrementTurnsWithoutFluoxetine() {
        turnsWithoutFluoxetine = turnsWithoutFluoxetine + 1;
    }

    /**
     * Gets the current status effects affecting the player.
     *
     * @return An {@link EnumSet} of {@link StatusEffect} representing the player's current status effects
     */
    public Set<StatusEffect> statusEffects() {
        final EnumSet<StatusEffect> statusEffects = this.statusEffects.clone();

        if (currentHealth == 0) {
            statusEffects.add(StatusEffect.DEATH);
        }

        if (currentHealth < maximumHealth() * 0.3) {
            statusEffects.add(StatusEffect.INSANITY);
        }

        if (neuralToxicity >= 2) {
            statusEffects.add(StatusEffect.TARDIVE_DYSKINESIA);
        } else if (neuralToxicity == 1) {
            statusEffects.add(StatusEffect.DYSKINESIA);
        }

        if (Consumable.getUseCount(FluoxetineBottle.class) > 0) {
            statusEffects.add(StatusEffect.DEPENDENCY);
        }

        if (turnsWithoutFluoxetine >= 100 && statusEffects.contains(StatusEffect.DEPENDENCY)) {
            statusEffects.add(StatusEffect.DISCONTINUATION_SYNDROME);
        }

        for (final Equippable equippable : equippedItems.values()) {
            equippable.transformStatusEffects(statusEffects);
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
     * <p>
     * The returned list is a read-only copy of the inventory.
     *
     * @return A list of items in the player's inventory.
     */
    public List<Item> inventory() {
        return Collections.unmodifiableList(inventory);
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
    private int binarySearch(List<Item> list, String name) {
        if (list.isEmpty()) {
            return 0;
        } else if (name.compareToIgnoreCase(list.getFirst().name()) < 0) {
            return 0;
        } else if (name.compareToIgnoreCase(list.getLast().name()) > 0) {
            return list.size();
        }

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
            item.consume(this);
            inventory.remove(item);
        }
    }

    /**
     * Checks if the player has an item of the specified type in their inventory.
     *
     * @param itemType The class type of the item to check for.
     * @return True if the player has an item of the specified type, false otherwise.
     */
    public boolean hasItemOfType(Class<? extends Item> itemType) {
        for (Item item : inventory) {
            if (itemType.isInstance(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes an item of the specified type from the player's inventory.
     * <p>
     * If the player has more than one item of the specified type, only one instance will be removed.
     *
     * @param itemType The class type of the item to remove.
     */
    public void removeItemOfType(Class<? extends Item> itemType) {
        final Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (itemType.isInstance(item)) {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * Equips an equippable item to the player.
     *
     * @param item The equippable item to equip.
     */
    public void equipItem(Equippable item) {
        equippedItems.put(item.slot(), item);
    }

    /**
     * Gets the equipped item in the specified slot.
     *
     * @param slot The slot to check for an equipped item.
     * @return The equipped item in the specified slot, or null if no item is equipped.
     */
    public Equippable equippedItem(EquippableSlot slot) {
        return equippedItems.get(slot);
    }

    /**
     * Unequips an item from the specified slot.
     *
     * @param slot The slot from which to unequip the item.
     */
    public void unequipItem(EquippableSlot slot) {
        equippedItems.remove(slot);
    }

    /**
     * Gets the player's paint modified by the equipped armor.
     *
     * @param basePaint The base paint of the player.
     * @return The modified paint of the player.
     */
    public Paint equippedPaint(Paint basePaint) {
        final Equippable armor = equippedItem(EquippableSlot.ARMOR);
        if (armor == null) {
            return basePaint;
        } else {
            return armor.playerPaint(basePaint);
        }
    }
}
