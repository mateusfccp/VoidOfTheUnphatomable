package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.StatusEffect;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.StaticDissonance;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.FragmentOfNothingness;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.DoIfLastStepSucceeds;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.EffectedAttack;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveTowardsPlayer;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * An entity that represents a Static Dissonance enemy in the game.
 */
public final class StaticDissonanceEntity extends DamageableEntity<StaticDissonance> {
    private final Animation representationAnimation = Animation.repeating(Duration.ofMillis(1600));

    /**
     * Creates a new StaticDissonanceEntity at the given position on the given map.
     *
     * @param position The position of the static dissonance entity.
     * @param map      The map where the static dissonance entity is located.
     */
    public StaticDissonanceEntity(Offset position, Map map) {
        super(position, new StaticDissonance(), map);
        representationAnimation.play();
    }

    @Override
    public Character representation() {
        final int frame = (int) (representationAnimation.progress() * 4);
        return switch (frame) {
            case 1, 3 -> '*';
            case 2 -> '☼';
            default -> '•';
        };
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            final Player player = playerEntity.associatedObject();
            damage(player.attack(), player.hitChance());
        }
    }

    @Override
    public List<TurnStep> processTurn() {
        final Entity<Player> playerEntity = map().getEntitiesOfType(PlayerEntity.class).getFirst();
        final List<TurnStep> steps = new ArrayList<>();

        if (canSee(playerEntity)) {
            final int distance = distanceTo(playerEntity);
            final EffectedAttack<StaticDissonance> attack = new EffectedAttack<>(this, StatusEffect.DEPRESSION, 0.1);

            switch (distance) {
                case 1 -> steps.add(attack);
                case 2 -> {
                    steps.add(new MoveTowardsPlayer(this));
                    steps.add(
                        new DoIfLastStepSucceeds(attack)
                    );
                }
                default -> {
                    steps.add(new MoveTowardsPlayer(this));
                    steps.add(new MoveTowardsPlayer(this));
                }
            }
        }

        return steps;
    }

    @Override
    protected List<Item> loot() {
        final int dropQuantity = 1 + (int) (Math.random() * 2);

        return IntStream.range(0, dropQuantity)
            .<Item>mapToObj(_ -> new FragmentOfNothingness())
            .toList();
    }

    @Override
    protected void dispose() {
        super.dispose();
        representationAnimation.dispose();
    }
}
