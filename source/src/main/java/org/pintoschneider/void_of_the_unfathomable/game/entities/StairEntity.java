package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.scenes.InGame;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.List;

import static org.pintoschneider.void_of_the_unfathomable.game.engine.Engine.context;

public final class StairEntity extends Entity<Void> {
    public StairEntity(Offset position, Map map) {
        super(position, null, map);
    }

    boolean used = false;

    @Override
    public char representation() {
        return '≡';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public List<TurnStep> processTurn() {
        if (used) {
            return List.of(_ -> {
                context().sceneManager().pop();
                return true;
            });
        } else {
            return List.of();
        }
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity) {
            final Scene scene = context().sceneManager().currentScene();

            if (scene instanceof InGame inGameScene) {
                inGameScene.setDialog("""
                    You still don't have the Resounding Core, but you still decides to flee The Void for now.
                    You feel a brief moment of relief as you ascend back to the surface, but you know
                    that any hope for the world is now lost. The Void will continue to spread, consuming everything
                    in its path. COWARD.
                    """);

                used = true;
            }
        }
    }
}
