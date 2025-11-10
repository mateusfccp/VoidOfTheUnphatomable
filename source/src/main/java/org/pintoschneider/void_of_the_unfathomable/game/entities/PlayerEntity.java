package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;
import java.util.List;

public final class PlayerEntity extends Entity<Player> implements DamageableEntity {
    private final Animation damageAnimation = new Animation(Duration.ofMillis(100));

    public PlayerEntity(Offset position, Player associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    @Override
    public char representation() {
        return '@';
    }

    @Override
    public Paint paint() {
        final Paint basePaint = new Paint().withForegroundColor(ColorPalette.WHITE);
        final Paint equippedPaint = associatedObject().equippedPaint(basePaint);

        final Color color;
        if (damageAnimation.playing()) {
            color = Color.lerp(
                ColorPalette.VERMILION,
                equippedPaint.foregroundColor(),
                damageAnimation.progress()
            );
        } else {
            color = equippedPaint.foregroundColor();
        }

        return new Paint().withForegroundColor(color);
    }

    @Override
    public List<TurnStep> processTurn() {
        associatedObject().incrementTurnsWithoutFluoxetine();
        return List.of();
    }

    @Override
    public void damage(int amount) {
        associatedObject().damage(amount);
        damageAnimation.play();
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    protected void dispose() {
        damageAnimation.dispose();
    }
}
