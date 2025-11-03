package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.Colors;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;

public final class PlayerEntity extends Entity<Player> {
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
        final Color color = damageAnimation.playing() ? Color.lerp(Colors.DAMAGE, Color.WHITE, damageAnimation.progress()) : Color.WHITE;
        return new Paint().withForegroundColor(color);
    }

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
