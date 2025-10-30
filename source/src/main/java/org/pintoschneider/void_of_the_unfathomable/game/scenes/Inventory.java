package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.engine.Key;
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
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A scene representing the player's inventory, allowing them to view and interact with their items.
 */
public class Inventory extends SelectionScene {
    private final Player player;

    /**
     * Constructs an Inventory scene for the given player.
     *
     * @param player The player whose inventory is to be displayed.
     */
    public Inventory(Player player) {
        this.player = player;
    }

    @Override
    List<Option> options() {
        final List<ItemGroup> groups = groupedItems();

        return IntStream
            .range(0, groups.size())
            .mapToObj(this::getOptionForIndex)
            .toList();
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
            content = super.build();
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
                                        new Text("Atk:", Paint.BOLD),
                                        new SizedBox(0, 1),
                                        new EquippableDifference(
                                            player,
                                            Player::attack,
                                            getSelectedItem(),
                                            Equippable::attackModifier
                                        ),
                                        new SizedBox(0, 2),
                                        new Text("Def:", Paint.BOLD),
                                        new SizedBox(0, 1),
                                        new EquippableDifference(
                                            player,
                                            Player::defense,
                                            getSelectedItem(),
                                            Equippable::defenseModifier
                                        ),
                                        new SizedBox(0, 2),
                                        new Text("Crt:", Paint.BOLD),
                                        new SizedBox(0, 1),
                                        new EquippableDifference(
                                            player,
                                            Player::creativity,
                                            getSelectedItem(),
                                            Equippable::creativityModifier
                                        )
                                    ).
                                        mainAxisSize(MainAxisSize.MIN)
                                )
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
            Engine.context().sceneManager().pop(false);
        }

        super.onKeyPress(key);
    }

    private Item getSelectedItem() {
        final List<ItemGroup> groups = groupedItems();
        if (groups.isEmpty()) {
            return null;
        } else {
            return groups.get(currentIndex()).item;
        }
    }

    private SelectionScene.Option getOptionForIndex(int index) {
        final List<ItemGroup> groups = groupedItems();
        final ItemGroup group = groups.get(index);
        final boolean isKey = !(group.item instanceof Consumable) && !(group.item instanceof Equippable);
        final String suffix = isGroupEquipped(group) ? "(E)" : "   ";
        return new Option(
            "%s %s x%d".formatted(group.item.name(), suffix, group.count),
            this::processSelectedItem,
            !isKey
        );
    }

    private void processSelectedItem() {
        final Item selectedItem = getSelectedItem();

        switch (selectedItem) {
            case Consumable item -> {
                player.useItem(item);
                Engine.context().sceneManager().pop(true);
            }
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

class EquippableDifference extends Composent {
    final Player player;
    final Function<Player, Integer> playerAttributeGetter;
    final Item item;
    final Function<Equippable, Integer> equippableModifierGetter;

    EquippableDifference(
        Player player,
        Function<Player, Integer> playerAttributeGetter,
        Item item,
        Function<Equippable, Integer> equippableModifierGetter
    ) {
        this.player = player;
        this.item = item;
        this.equippableModifierGetter = equippableModifierGetter;
        this.playerAttributeGetter = playerAttributeGetter;
    }

    @Override
    public Component build() {
        final int originalValue = playerAttributeGetter.apply(player);

        if (!(item instanceof Equippable equippable)) {
            return new Text("%d".formatted(originalValue), Paint.DIM);
        }

        final boolean isEquipped = player.equippedItem(equippable.slot()) == equippable;

        final int newValue = originalValue + equippableModifierGetter.apply(equippable) * (isEquipped ? -1 : 1);

        final boolean hasDifference = originalValue != newValue;
        final Paint oldValuePaint = hasDifference ? null : Paint.DIM;
        final Paint newValuePaint;

        if (newValue > originalValue) {
            newValuePaint = new Paint().withForegroundColor(Color.GREEN);
        } else if (newValue < originalValue) {
            newValuePaint = new Paint().withForegroundColor(Color.RED);
        } else {
            newValuePaint = Paint.DIM;
        }

        return new Row(
            new Text("%d".formatted(originalValue), oldValuePaint),
            new Text(" → ", Paint.DIM),
            new Text("%d".formatted(newValue), newValuePaint)
        );
    }
}
