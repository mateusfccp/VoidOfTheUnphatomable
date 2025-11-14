package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.FluoxetineBottle;
import org.pintoschneider.void_of_the_unfathomable.game.items.consumables.HaloperidolAmpoule;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.MaidDress;
import org.pintoschneider.void_of_the_unfathomable.game.items.equippables.armors.Sunga;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.ShopScene;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * An entity that represents a Merchant allowing the player to interact and buy various items.
 * <p>
 * When the player interacts with this entity, the ShopScene scene is called.
 */
public class ShopKeeperEntity extends Entity<Void> {
    final Animation idleAnimation = Animation.repeating(
        Duration.ofSeconds(2)
    );
    /**
     * The list of items this shopkeeper will sell.
     */
    private final List<Item> shopItems;
    private final Player player;
    boolean isFirstInteraction = true;

    /**
     * Creates a new ShopKeeperEntity.
     *
     * @param position The position of the shopkeeper.
     * @param map      The map the shopkeeper is in.
     * @param player   The player object.
     */
    public ShopKeeperEntity(Offset position, Map map, Player player) {
        super(position, null, map);
        this.shopItems = createShopStock();
        this.player = player;
        idleAnimation.play();
    }

    /**
     * Creates the sorted list of items this shopkeeper will sell.
     */
    private List<Item> createShopStock() {
        List<Item> stock = new ArrayList<>();
        addItemToStock(stock, new MaidDress());
        addItemToStock(stock, new Sunga());
        for (int i = 0; i < 99; i++) {
            addItemToStock(stock, new FluoxetineBottle());
            addItemToStock(stock, new HaloperidolAmpoule());
        }
        return stock;
    }

    @Override
    public Character representation() {
        if (player.statusEffects().contains(StatusEffect.INSANITY)) {
            return '$';
        } else {
            final int frame = (int) (idleAnimation.progress() * 4);

            return switch (frame) {
                case 1, 3 -> '▒';
                case 2 -> '▓';
                default -> '░';
            };
        }
    }

    @Override
    public Paint paint() {
        final Paint basePaint = new Paint().withForegroundColor(ColorPalette.PINE_GREEN);

        if (player.statusEffects().contains(StatusEffect.INSANITY)) {
            return basePaint;
        } else {
            return basePaint.withDim(true);
        }
    }

    @Override
    public SpatialProperty spatialProperty() {
        if (player.statusEffects().contains(StatusEffect.INSANITY)) {
            return new SpatialProperty(false, true);
        } else {
            return new SpatialProperty(false, false);
        }
    }

    @Override
    public void interact(Entity<?> entity) {
        if (player.statusEffects().contains(StatusEffect.INSANITY)) {
            if (entity instanceof PlayerEntity playerEntity) {
                if (isFirstInteraction) {
                    Engine.context().sceneManager().push(
                        new DialogScene(
                            "¿Podés verme? Hmmm... Eso quiere decir que tu mente está lo suficientemente perturbada como para percibirme. Pero tal vez eso no sea tan malo... ¿Sabés estes Fragmentos de Nulidad que están por todos lados? Puedo ofrecerte algunos objetos a cambio de ellos, si te interesan.",
                            Alignment.CENTER
                        )
                    ).thenRun(() -> {
                        Player player = playerEntity.associatedObject();
                        Engine.context().sceneManager().push(
                            new ShopScene(player, this.shopItems)
                        );

                        isFirstInteraction = false;
                    });

                } else {
                    Player player = playerEntity.associatedObject();
                    Engine.context().sceneManager().push(
                        new ShopScene(player, this.shopItems)
                    );
                }
            }
        } else {
            Engine.context().sceneManager().push(
                new DialogScene(
                    "Sentís una débil presencia, pero tu mente no puede comprenderla... Te parece que estás alucinando. O... tal vez... ¿estés muy sano?",
                    Alignment.CENTER
                )
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

    @Override
    protected void dispose() {
        idleAnimation.dispose();
    }
}
