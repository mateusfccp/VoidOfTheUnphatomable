package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.Abyssmonkey;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.FamilyPhoto;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.ResoundingCore;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.CreateEntity;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveTowards;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveTowardsPlayer;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * An entity that represents the abyssmonkey enemy in the game.
 */
public class AbyssmonkeyEntity extends DamageableEntity<Abyssmonkey> {
    private boolean didStealPhoto = false;
    private int turnsSinceLastTranana = 0;
    private static final Random random = new Random();
    private static final int movementSpeed = 2;

    /**
     * Creates a new AbyssmonkeyEntity at the given position on the given map.
     *
     * @param position The position of the abyssmonkey entity.
     * @param map      The map where the abyssmonkey entity is located.
     */
    public AbyssmonkeyEntity(Offset position, Map map) {
        super(position, new Abyssmonkey(), map);
    }

    @Override
    public Character representation() {
        return '☻';
    }

    @Override
    protected Paint basePaint() {
        return new Paint().withForegroundColor(ColorPalette.APRICOT);
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            final Player player = playerEntity.associatedObject();
            damage(player.attack(), player.hitChance());
        }
    }

    @Override
    public List<TurnStep> processTurn() {
        final PlayerEntity playerEntity = map().getEntitiesOfType(PlayerEntity.class).getFirst();
        final int distanceToPlayer = this.distanceTo(playerEntity);

        if (didStealPhoto) {
            Offset target;

            // Find a random position around the abyssmonkey to move towards
            do {
                final int dx = random.nextInt(-movementSpeed, movementSpeed + 1);
                final int dy = random.nextInt(-movementSpeed, movementSpeed + 1);
                target = position().add(new Offset(dx, dy));
            } while (!map().walkable(target));

            final int distance = this.distanceTo(target);

            final Offset finalTarget = target;

            turnsSinceLastTranana = turnsSinceLastTranana + 1;

            final List<TurnStep> steps = new ArrayList<>();

            if (turnsSinceLastTranana == 4) {
                turnsSinceLastTranana = 0;

                steps.add(
                    new CreateEntity(
                        this,
                        () -> new TrananaEntity(position(), map())
                    )
                );
            }

            for (int i = 0; i < distance; i++) {
                steps.add(new MoveTowards(this, finalTarget));
            }

            return steps;
        } else {
            if (distanceToPlayer <= 13) {
                final List<TurnStep> walkTowardsPlayer = IntStream.range(0, distanceToPlayer - 1)
                    .<TurnStep>mapToObj(_ -> new MoveTowardsPlayer(this))
                    .toList();

                final List<TurnStep> stealPhoto = List.of(
                    _ -> {
                        didStealPhoto = true;
                        final Player player = playerEntity.associatedObject();
                        player.removeItemOfType(FamilyPhoto.class);

                        Engine.context().sceneManager().push(
                            new DialogScene(
                                "El abismono te robó la foto de familia y se la guardó en su bolsillo.",
                                Alignment.CENTER
                            )
                        );

                        return true;
                    }
                );

                final List<TurnStep> walkBack = IntStream.range(0, distanceToPlayer - 1)
                    .<TurnStep>mapToObj(_ -> new MoveTowards(this, position()))
                    .toList();

                final List<TurnStep> offend = List.of(
                    _ -> {
                        Engine.context().sceneManager().push(
                            new DialogScene(
                                "\"Uh-uh-uh-aha-aha!\" grita el abismono mientras se aleja corriendo con tu foto de familia.\n\n\"Ni siquiera es tu familia, ¡boludo!\"",
                                Alignment.CENTER
                            )
                        );

                        return true;
                    }
                );

                return Stream.of(
                        walkTowardsPlayer,
                        stealPhoto,
                        walkBack,
                        offend
                    )
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            }
        }

        return List.of();
    }

    @Override
    protected List<Item> loot() {
        return List.of(
            new FamilyPhoto(),
            new ResoundingCore()
        );
    }
}
