package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.animation.Animation;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Damageable;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Color;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.time.Duration;
import java.util.List;

/**
 * An entity that can be damaged.
 */
public abstract class DamageableEntity<T extends Damageable> extends Entity<T> {
    protected final Animation damageAnimation = new Animation(Duration.ofMillis(100));

    protected DamageableEntity(Offset position, T associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    @Override
    public Paint paint() {
        final Color color = damageAnimation.playing() ? Color.lerp(ColorPalette.VERMILION, ColorPalette.WHITE, damageAnimation.progress()) : null;
        return new Paint().withForegroundColor(color);
    }

    /**
     * Damages the associated object and destroy the entity if necessary.
     *
     * @param amount The amount of damage to deal to the associated object.
     */
    final public void damage(int amount) {
        damageAnimation.play();
        final boolean result = this.associatedObject().damage(amount);

        if (result) {
            final List<Item> loot = loot();

            for (final Item item : loot) {
                drop(item);
            }

            destroy();
        }
    }

    /**
     * The items that should be dropped by the entity when it is destroyed.
     *
     * @return A list with the items that should be dropped when the entity is destroyed.
     */
    protected List<Item> loot() {
        return List.of();
    }

    @Override
    protected void dispose() {
        damageAnimation.dispose();
    }
}
