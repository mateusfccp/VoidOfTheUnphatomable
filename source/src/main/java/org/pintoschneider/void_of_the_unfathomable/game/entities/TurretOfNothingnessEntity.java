package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.TurretOfNothingness;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.List;

/**
 * An entity representing a Turret of Nothingness.
 */
public class TurretOfNothingnessEntity extends DamageableEntity<TurretOfNothingness> {
    private boolean isShootingDiagonal = false;
    private int turnCount = 0;

    /**
     * Creates a new TurretOfNothingnessEntity.
     *
     * @param position The position of the turret.
     * @param map      The map the turret is in.
     */
    public TurretOfNothingnessEntity(Offset position, Map map) {
        super(position, new TurretOfNothingness(), map);
    }

    @Override
    public void damage(int amount) {
    }

    @Override
    public Character representation() {
        return '✱';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, true);
    }

    @Override
    public List<TurnStep> processTurn() {
        turnCount = (turnCount + 1) % 4;

        if (turnCount == 0) {
            return shoot();
        } else {
            return List.of();
        }
    }

    private List<TurnStep> shoot() {
        if (isShootingDiagonal) {
            isShootingDiagonal = false;
            return List.of(
                _ -> {
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.NORTH_WEST);
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.NORTH_EAST);
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.SOUTH_WEST);
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.SOUTH_EAST);

                    return true;
                }
            );
        } else {
            isShootingDiagonal = true;

            return List.of(
                _ -> {
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.NORTH);
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.SOUTH);
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.EAST);
                    new BulletEntity(position(), map(), associatedObject().attack(), Offset.WEST);

                    return true;
                }
            );
        }
    }
}
