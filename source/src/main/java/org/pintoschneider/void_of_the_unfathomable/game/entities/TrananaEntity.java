package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

/**
 * An entity that represents a tranana in the game.
 */
public class TrananaEntity extends Entity<Void> {
    /**
     * Creates a new TrananaEntity at the given position on the given map.
     *
     * @param position The position of the tranana entity.
     * @param map      The map where the tranana entity is located.
     */
    public TrananaEntity(Offset position, Map map) {
        super(position, null, map);
    }

    @Override
    public Character representation() {
        return ')';
    }

    @Override
    public Paint paint() {
        return new Paint().withForegroundColor(ColorPalette.BANANA);
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(true, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            playerEntity.associatedObject().damage(5);
            destroy();
        }
    }
}
