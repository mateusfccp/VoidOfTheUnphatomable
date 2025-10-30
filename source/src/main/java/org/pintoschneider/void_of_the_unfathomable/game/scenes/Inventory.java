package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.items.Consumable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.EquippableSlot;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A scene representing the player's inventory, allowing them to view and interact with their items.
 */
public class Inventory implements Scene {
    private final Player player;
    private int currentIndex = 0;

    /**
     * Constructs an Inventory scene for the given player.
     *
     * @param player The player whose inventory is to be displayed.
     */
    public Inventory(Player player) {
        this.player = player;
    }

    @Override
    public Component build() {
        final Component content;

        if (player.inventory().isEmpty()) {
            content =
                new ConstrainedBox(
                    Constraints.tight(40, 20),
                    new Align(
                        Alignment.CENTER,
                        new Text("Inventario vacío", Paint.DIM)
                    )
                );
        } else {
            final List<ItemGroup> groups = groupedItems();

            final Component[] items = IntStream
                .range(0, groups.size())
                .mapToObj(this::getComponentForIndex)
                .toArray(Component[]::new);

            content = new Column(items)
                .mainAxisSize(MainAxisSize.MIN);
        }

        return new Padding(
            EdgeInsets.all(1),
            new Align(
                Alignment.TOP_CENTER,
                new Column(
                    new Box(
                        Border.SINGLE_ROUNDED,
                        new Text("Inventario")
                    ),
                    new SizedBox(0, 1),
                    new ConstrainedBox(
                        new Constraints(40, null, 20, null),
                        new Box(
                            Border.SINGLE_ROUNDED,
                            new Padding(
                                EdgeInsets.all(1),
                                content
                            )
                        )
                    ),
                    new Flexible(new SizedBox(0, 0)),
                    new ConstrainedBox(
                        new Constraints(0, null, 8, 8),
                        new Box(
                            Border.SINGLE_ROUNDED,
                            new Text(Objects.requireNonNullElse(getSelectedItem().description(), ""))
                        )
                    )
                ).crossAxisAlignment(CrossAxisAlignment.CENTER)
            )
        );
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.I || key == Key.ENTER && player.inventory().isEmpty()) {
            Engine.context().sceneManager().pop();
            return;
        }

        final List<ItemGroup> groups = groupedItems();
        if (groups.isEmpty()) return;

        if (key == Key.UP) {
            currentIndex = (currentIndex - 1 + groups.size()) % groups.size();
        } else if (key == Key.DOWN) {
            currentIndex = (currentIndex + 1) % groups.size();
        } else if (key == Key.ENTER) {
            final Item selectedItem = getSelectedItem();

            switch (selectedItem) {
                case Consumable item -> player.useItem(item);
                case Equippable equippable -> {
                    if (player.equippedItem(equippable.slot()) == equippable) {
                        player.unequipItem(equippable.slot());
                    } else {
                        player.equipItem(equippable);
                    }
                }
                case null, default -> {
                    // Do nothing
                }
            }
        }
    }

    private Item getSelectedItem() {
        final List<ItemGroup> groups = groupedItems();
        if (groups.isEmpty()) {
            return null;
        } else {
            return groups.get(currentIndex).item;
        }
    }

    private Component getComponentForIndex(int index) {
        final List<ItemGroup> groups = groupedItems();
        final ItemGroup group = groups.get(index);
        final boolean isCurrent = index == currentIndex;
        final boolean isKey = !(group.item instanceof Consumable) && !(group.item instanceof Equippable);
        final Paint paint;

        if (isCurrent) {
            paint = Paint.INVERTED;
        } else if (isKey) {
            paint = Paint.DIM;
        } else {
            paint = null;
        }

        String displayName = group.item.name();
        if (isGroupEquipped(group)) {
            displayName += " (E)";
        }

        return new Text(
            "%s x%d".formatted(displayName, group.count),
            paint
        );
    }

    private boolean isGroupEquipped(ItemGroup group) {
        if (!(group.item instanceof Equippable)) return false;

        for (EquippableSlot slot : EquippableSlot.values()) {
            final Equippable equipped = player.equippedItem(slot);
            if (equipped != null && equipped.getClass().equals(group.item.getClass())) {
                return true;
            }
        }
        return false;
    }

    private List<ItemGroup> groupedItems() {
        final LinkedHashMap<Class<? extends Item>, ItemGroup> map = new LinkedHashMap<>();

        for (Item item : player.inventory()) {
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
