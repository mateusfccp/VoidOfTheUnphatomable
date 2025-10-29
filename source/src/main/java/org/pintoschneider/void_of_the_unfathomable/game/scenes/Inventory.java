package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.items.Consumable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Equippable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.ui.components.*;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.Map;
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
            final Component[] items = IntStream
                .range(0, player.inventory().size())
                .mapToObj(this::getItemAtIndex)
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

        if (player.inventory().isEmpty()) {
            return;
        }

        if (key == Key.UP) {
            currentIndex = (currentIndex - 1 + player.inventory().size()) % player.inventory().size();
        } else if (key == Key.DOWN) {
            currentIndex = (currentIndex + 1) % player.inventory().size();
        } else if (key == Key.ENTER) {
            final Item selectedItem = getSelectedItem();

            switch (selectedItem) {
                case Consumable item -> player.useItem(item);
                case Equippable equippable -> player.equipItem(equippable);
                case null, default -> {
                    // Do nothing
                }
            }
        }
    }

    private Item getSelectedItem() {
        if (player.inventory().isEmpty()) {
            return null;
        } else {
            return player.inventory().get(currentIndex);
        }
    }

    private Component getItemAtIndex(int index) {
        final Item item = player.inventory().get(index);
        return new Text(
            item.name(),
            index == currentIndex ? Paint.INVERTED : null
        );
    }

    private Map<Class<? extends Item>, Integer> itemQuantity() {
        return player.inventory().stream()
            .collect(
                java.util.stream.Collectors.groupingBy(
                    Item::getClass,
                    java.util.stream.Collectors.summingInt(e -> 1)
                )
            );
    }
}
