package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.LeftBanana;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.RightBanana;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;

/**
 * An entity representing a locked door that can be unlocked with specific key items.
 */
public class LockedDoor extends Entity<Boolean> {
    static Boolean isLocked = true;

    /**
     * Creates a new LockedDoor entity at the specified position on the given map.
     *
     * @param position The position of the locked door.
     * @param map      The map the locked door is in.
     */
    public LockedDoor(Offset position, Map map) {
        super(position, isLocked, map);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity player) {
            final boolean hasLeftBanana = player.associatedObject().hasItemOfType(LeftBanana.class);
            final boolean hasRightBanana = player.associatedObject().hasItemOfType(RightBanana.class);
            if (hasLeftBanana && hasRightBanana && isLocked) {
                isLocked = false;
                player.associatedObject().removeItemOfType(LeftBanana.class);
                player.associatedObject().removeItemOfType(RightBanana.class);
            } else if (isLocked) {
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "La puerta está cerrada y necesita de una banana para abrirse.",
                        Alignment.CENTER
                    )
                );
            }
        }
    }

    @Override
    public Character representation() {
        if (isLocked) {
            return '▀';
        } else {
            return null;
        }
    }

    @Override
    public SpatialProperty spatialProperty() {
        if (isLocked) {
            return new SpatialProperty(false, true);
        } else {
            return new SpatialProperty(true, false);
        }
    }
}
