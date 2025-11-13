package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.LeftBanana;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.RightBanana;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapTile;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;

/**
 * An entity that represents a locked door that can be unlocked with specific key items.
 */
public class LockedDoor extends Entity<Boolean> {
    private static Boolean isLocked = true;

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
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "Al juntar Bnn y aaa, una banana completa aparece y es absorbida por la cerradura mónica. La puerta se abre. Al fondo se ve un pasillo oscuro, y se puede escuchar un leve susurro que dice \"Uh... Uh... Uh-uh-haah!\"",
                        Alignment.CENTER
                    )
                ).thenRun(() -> {
                    isLocked = false;
                    player.associatedObject().removeItemOfType(LeftBanana.class);
                    player.associatedObject().removeItemOfType(RightBanana.class);

                    // This shouldn't be hardcoded, but what do I know?
                    for (int x = 108; x <= 111; x++) {
                        map().setTileAt(x, 101, MapTile.FLOOR);
                    }
                });
            } else if (isLocked) {
                Engine.context().sceneManager().push(
                    new DialogScene(
                        "La puerta está cerrada. Hay una ranura en forma de banana en la cerradura.",
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

    /**
     * Whether the door is locked.
     *
     * @return True if the door is locked, false if it is not.
     */
    static public boolean isLocked() {
        return isLocked;
    }
}
