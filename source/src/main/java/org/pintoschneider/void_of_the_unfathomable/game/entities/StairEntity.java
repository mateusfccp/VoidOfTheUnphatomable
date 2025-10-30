package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.engine.Engine;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.DialogScene;

public final class StairEntity extends Entity<Void> {
    public StairEntity(Offset position, Map map) {
        super(position, null, map);
    }

    @Override
    public char representation() {
        return '≡';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity) {
            Engine.context().sceneManager().push(
                new DialogScene(
                    "You still don't have the Resounding Core, but you still decides to flee The Void for now. You feel a brief moment of relief as you ascend back to the surface, but you know that any hope for the world is now lost. The Void will continue to spread, consuming everything in its path. COWARD.",
                    c -> Engine.context().sceneManager().pop()
                )
            );
        }
    }
}
