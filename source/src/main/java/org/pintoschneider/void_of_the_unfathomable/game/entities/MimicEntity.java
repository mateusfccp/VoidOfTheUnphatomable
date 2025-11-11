package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.Mimic;
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

public final class MimicEntity extends DamageableEntity<Mimic> {
        private final Animation representationAnimation = Animation.repeating(Duration.ofMillis(1600));
    private final Animation damageAnimation = new Animation(Duration.ofMillis(100));

    public MimicEntity(Offset position, Map map) {
        super(position, new Mimic(), map);
        representationAnimation.play();
    }

    @Override
    public Character representation() {
        if (this.associatedObject().state() == Mimic.State.IDLE) {
            return '▆';
        }

        final int frame = (int) (representationAnimation.progress() * 2);
        return switch (frame) {
            case 1 -> '▆';
            default -> '█';
        };
    }

    @Override
    public Paint paint() {
        final Color color = damageAnimation.playing() ? Color.lerp(ColorPalette.VERMILION, ColorPalette.WHITE, damageAnimation.progress()) : null;
        return new Paint().withForegroundColor(color);
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (this.associatedObject().state() == Mimic.State.IDLE) {
            associatedObject().followPlayer();
        } else if (entity instanceof PlayerEntity playerEntity) {
            final Player player = playerEntity.associatedObject();
            damage(player.attack());
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

    public void damage(int amount) {
        this.associatedObject().damage(amount);
        damageAnimation.play();

        if (this.associatedObject().health() == 0) {
            final int dropQuantity = 1 + (int) (Math.random() * 2);
            drop(new FragmentOfNothingness(), dropQuantity);
            destroy();
        }
    }

    @Override
    protected void dispose() {
        damageAnimation.dispose();
        representationAnimation.dispose();
    }
}