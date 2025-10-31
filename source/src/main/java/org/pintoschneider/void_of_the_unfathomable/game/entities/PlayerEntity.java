package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;

public final class PlayerEntity extends Entity<Player> {
    final Animation animation = new Animation(Duration.ofMillis(200));
    private static final Color damageColor = new Color(237, 119, 90);

    public PlayerEntity(Offset position, Player associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    @Override
    public char representation() {
        return '@';
    }

    @Override
    public Paint paint() {
        final Color color = animation.playing() ? Color.lerp(damageColor, Color.WHITE, animation.progress()) : Color.WHITE;
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
