package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

/**
 * An entity representing an item on the map.
 * <p>
 * When a player interacts with this entity, the item is added to the player's inventory and the entity is destroyed.
 */
public final class ItemEntity extends Entity<Item> {
    private final Paint paint;

    public ItemEntity(Offset position, Item item, Map map) {
        this(position, item, null, map);
    }

    public ItemEntity(Offset position, Item item, Paint paint, Map map) {
        super(position, item, map);
        this.paint = paint;
    }

    @Override
    public Character representation() {
        return '○';
    }

    @Override
    public Paint paint() {
        return paint;
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(true, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            playerEntity.associatedObject().addItemToInventory(associatedObject());
            destroy();
        }
    }
}
