package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;

public final class ItemEntity extends Entity<Item> {
    public ItemEntity(Offset position, Item associatedObject, Map map) {
        super(position, associatedObject, map);
    }

    @Override
    public char representation() {
        return '○';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(true, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            playerEntity.associatedObject().addItemToInventory(associatedObject());
            map().removeEntity(this);
        }
    }
}
