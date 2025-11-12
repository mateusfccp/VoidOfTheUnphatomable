package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.ShopScene;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.FluoxetineBottle;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.HaloperidolAmpoule;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.MaidDress;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity representing a Merchant allowing the player to interact and buy various items.
 * <p>
 * When the player interacts with this entity, the ShopScene scene is called.
 */
public class ShopKeeperEntity extends Entity<Void> {

    /**
     * The list of items this shopkeeper will sell.
     */
    private final List<Item> shopItems;

    public ShopKeeperEntity(Offset position, Map map) {
        super(position, null, map);
        this.shopItems = createShopStock();
    }

    /**
     * Creates the sorted list of items this shopkeeper will sell.
     */
    private List<Item> createShopStock() {
        List<Item> stock = new ArrayList<>();
        addItemToStock(stock, new MaidDress());
        for (int i = 0; i < 99; i++) {
            addItemToStock(stock, new FluoxetineBottle());
            addItemToStock(stock, new HaloperidolAmpoule());
        }
        return stock;
    }

    @Override
    public Character representation() {
        return '$';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, true);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            Player player = playerEntity.associatedObject();
            Engine.context().sceneManager().push(
                new ShopScene(player, this.shopItems)
            );
        }
    }

    /**
     * Adds an item to the shop's stock list, maintaining alphabetical sort order.
     *
     * @param stock The list to add to.
     * @param item  The item to add.
     */
    private void addItemToStock(List<Item> stock, Item item) {
        int index = binarySearch(stock, item.name());
        if (index == stock.size()) {
            stock.add(item);
        } else {
            stock.add(index, item);
        }
    }

    /**
     * Searches the stock list with an efficiency of O(log n).
     *
     * @param list The list to search.
     * @param name The name of the item to be added.
     * @return The index of where to place the item in the List.
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

            if (mid == list.size()) {
                return mid;
            }

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
}