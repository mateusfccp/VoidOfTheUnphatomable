package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity that manages all bullets in the game.
 */
public class BulletManagerEntity extends Entity<Void> {
    private final List<BulletEntity> bullets = new ArrayList<>();

    /**
     * Creates a new BulletManagerEntity.
     *
     * @param position The position of the bullet manager.
     * @param map      The map the bullet manager is in.
     */
    public BulletManagerEntity(Offset position, Map map) {
        super(position, null, map);
    }

    @Override
    public Character representation() {
        return null;
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public List<TurnStep> processTurn() {
        return bullets.stream()
            .flatMap(bullet -> bullet.move().stream())
            .toList();
    }

    /**
     * Adds a bullet to the bullet manager.
     *
     * @param bullet The bullet to add.
     */
    public void addBullet(BulletEntity bullet) {
        bullets.add(bullet);
    }

    /**
     * Removes a bullet from the bullet manager.
     *
     * @param bullet The bullet to remove.
     */
    public void removeBullet(BulletEntity bullet) {
        bullets.remove(bullet);
    }
}
