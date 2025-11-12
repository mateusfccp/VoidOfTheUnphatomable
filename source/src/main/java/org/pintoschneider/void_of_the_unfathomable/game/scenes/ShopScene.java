package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A scene representing a shop, allowing the player to buy items.
 */
public class ShopScene extends SelectionScene {

    private final Player player;
    private final List<Item> shopStock;
    private List<ItemGroup> groupedShopItems;

    /**
     * Constructs a Shop scene.
     *
     * @param player    The player who is shopping.
     * @param shopItems The list of items available for sale.
     */
    public ShopScene(Player player, List<Item> shopItems) {
        this.player = player;
        this.shopStock = shopItems;
        this.groupedShopItems = groupItems(shopItems);
    }

    @Override
    List<Option> options() {
        this.groupedShopItems = groupItems(this.shopStock);
        return IntStream
            .range(0, groupedShopItems.size())
            .mapToObj(this::getOptionForIndex)
            .toList();
    }

    @Override
    public Component build() {
        final Component content;

        if (shopStock.isEmpty()) {
            content =
                new ConstrainedBox(
                    Constraints.tight(40, 20),
                    new Align(
                        Alignment.CENTER,
                        new Text("Tienda vacía", Paint.DIM)
                    )
                );
        } else {
            content = super.build();
        }

        final Item selectedItem = getSelectedItem();
        return new Padding(
            EdgeInsets.all(1),
            new Align(
                Alignment.TOP_CENTER,
                new Column(
                    new Box(
                        Border.SINGLE_ROUNDED,
                        new Text("Tienda") // Changed title
                    ),
                    new SizedBox(0, 1),
                    new Row(
                        new ConstrainedBox(
                            new Constraints(40, null, 20, null),
                            content
                        ),
                        new ConstrainedBox(
                            new Constraints(16, null, 20, null),
                            new Box(
                                Border.SINGLE_ROUNDED,
                                new Padding(
                                    EdgeInsets.all(1),
                                    new Column(
                                        new SizedBox(0, 2),
                                        new Text("Fragmentos:", Paint.BOLD),
                                        new SizedBox(0, 1),
                                        new Text("%d F".formatted(player.getFragmentCount()))
                                    ).mainAxisSize(MainAxisSize.MIN)
                                )
                            )
                        )
                    ).mainAxisSize(MainAxisSize.MIN),
                    selectedItem == null ? null : new Flexible(new SizedBox(0, 0)),
                    selectedItem == null ? null : new ConstrainedBox(
                        new Constraints(0, null, 8, 8),
                        new Box(
                            Border.SINGLE_ROUNDED,
                            new Text(selectedItem.description())
                        )
                    )
                ).crossAxisAlignment(CrossAxisAlignment.CENTER)
            )
        );
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.ESC || key == Key.BACKSPACE) {
            Engine.context().sceneManager().pop(false);
        }

        if (key == Key.ENTER && shopStock.isEmpty()) {
            Engine.context().sceneManager().pop(false);
        }

        super.onKeyPress(key);
    }

    /**
     * Gets the currently selected item from the grouped list.
     */
    private Item getSelectedItem() {
        if (groupedShopItems.isEmpty() || currentIndex() < 0 || currentIndex() >= groupedShopItems.size()) {
            return null;
        } else {
            return groupedShopItems.get(currentIndex()).item;
        }
    }

    /**
     * Creates a display Option for an item, showing its name, count, and price.
     */
    private SelectionScene.Option getOptionForIndex(int index) {
        final ItemGroup group = groupedShopItems.get(index);

        final int price = group.item.price();

        return new Option(
            "%s x%d (%dF)".formatted(group.item.name(), group.count, price),
            this::processSelectedItem,
            true
        );
    }

    /**
     * Handles the "buy" action when an item is selected.
     */
    private void processSelectedItem() {
        final Item selectedItem = getSelectedItem();
        if (selectedItem == null) return;

        final int price = selectedItem.price();

        if (player.getFragmentCount() >= price) {
            player.removeFragments(price);
            player.addItemToInventory(selectedItem);
            shopStock.remove(selectedItem);
        }
    }

    /**
     * Groups a list of items by class and count.
     */
    private List<ItemGroup> groupItems(List<Item> items) {
        final LinkedHashMap<Class<? extends Item>, ItemGroup> map = new LinkedHashMap<>();

        for (Item item : items) {
            final Class<? extends Item> key = item.getClass();
            final ItemGroup existing = map.get(key);
            if (existing == null) {
                map.put(key, new ItemGroup(item));
            } else {
                existing.count();
            }
        }

        return new ArrayList<>(map.values());
    }

    /**
     * Helper class to store a grouped item and its count.
     */
    private static final class ItemGroup {
        final Item item;
        int count;

        ItemGroup(Item item) {
            this.item = item;
            this.count = 1;
        }

        void count() {
            count = count + 1;
        }
    }
}
