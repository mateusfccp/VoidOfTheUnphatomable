package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.Mimic;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.FragmentOfNothingness;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.AreaAttack;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveTowardsPlayer;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A mimic entity that represents a mimic enemy in the game.
 */
public final class MimicEntity extends DamageableEntity<Mimic> {
    private final Animation representationAnimation = Animation.repeating(Duration.ofMillis(800));

    /**
     * Creates a new MimicEntity at the given position on the given map.
     *
     * @param position The position of the mimic entity.
     * @param map      The map where the mimic entity is located.
     */
    public MimicEntity(Offset position, Map map) {
        super(position, new Mimic(), map);
        representationAnimation.play();
    }

    @Override
    public Character representation() {
        if (this.associatedObject().state() == Mimic.State.IDLE) {
            return '▆';
        }

        final int frame = (int) (representationAnimation.progress() * 4);
        return switch (frame) {
            case 1, 3 -> '▆';
            case 2 -> '▅';
            default -> '█';
        };
    }

    @Override
    protected Paint basePaint() {
        return new Paint().withForegroundColor(ColorPalette.APRICOT);
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

            if (this.associatedObject().state() == Mimic.State.IDLE) {
                associatedObject().followPlayer();
            }
        }
    }

    @Override
    public List<TurnStep> processTurn() {
        switch (this.associatedObject().state()) {
            case IDLE -> {
                return List.of();
            }
            case FOLLOWING_PLAYER -> {
                associatedObject().attackPlayer();
                return List.of(
                    new MoveTowardsPlayer(this)
                );
            }
            case ATTACKING -> {
                // TODO: IMPLMEMENT ANUMATION
                associatedObject().followPlayer();
                return List.of(new AreaAttack<>(this, 3));
            }
            default -> throw new IllegalStateException("Unexpected value: " + this.associatedObject().state());
        }
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