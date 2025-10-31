package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;

public final class PlayerEntity extends Entity<Player> {
    final Animation animation = new Animation(Duration.ofMillis(200));

    public PlayerEntity(Offset position, Player associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    @Override
    public char representation() {
        return '@';
    }

    @Override
    public Paint paint() {
        // Color based on animation progress
        final double progress = animation.progress();
        final short r = 255;
        final short g = (short) (255 * progress);
        final short b = (short) (255 * progress);
        final Color color = new Color(r, g, b);
        return new Paint().withForegroundColor(color);
    }

    // TODO(mateusfccp): Refactor this
    public void playDamageAnimation() {
        animation.play();
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }
}
