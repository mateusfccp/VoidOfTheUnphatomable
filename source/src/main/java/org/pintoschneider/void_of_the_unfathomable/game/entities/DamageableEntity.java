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
import java.util.Random;

/**
 * An entity that can be damaged.
 */
public abstract class DamageableEntity<T extends Damageable> extends Entity<T> {
    private static final Random random = new Random();
    protected final Animation damageAnimation = new Animation(Duration.ofMillis(100));

    protected DamageableEntity(Offset position, T associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    /**
     * The base paint of the entity, without any damage effects.
     *
     * @return The base paint of the entity.
     */
    protected Paint basePaint() {
        return new Paint().withForegroundColor(ColorPalette.WHITE);
    }

    @Override
    public Paint paint() {
        final Color baseColor = basePaint().foregroundColor();
        final Color color = damageAnimation.playing() ? Color.lerp(
            ColorPalette.VERMILION,
            baseColor,
            damageAnimation.progress()
        ) : baseColor;
        return basePaint().withForegroundColor(color);
    }

    /**
     * Damages the associated object and destroy the entity if necessary.
     *
     * @param amount The amount of damage to deal to the associated object.
     */
    final public void damage(int amount) {
        damage(amount, 1.0);
    }

    /**
     * Damages the associated object and destroy the entity if necessary.
     *
     * @param amount    The amount of damage to deal to the associated object.
     * @param hitChance The chance to hit the target (between 0.0 and 1.0).
     */
    final public void damage(int amount, double hitChance) {
        if (random.nextDouble() < hitChance) {
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
