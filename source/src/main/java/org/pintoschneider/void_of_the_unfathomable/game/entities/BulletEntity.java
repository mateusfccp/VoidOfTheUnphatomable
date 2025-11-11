package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.DoIfLastStepFails;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveInDirection;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.List;

/**
 * An entity representing a bullet in the game.
 */
public class BulletEntity extends Entity<Void> {
    final private int damage;
    final private Offset direction;
    final BulletManagerEntity manager;

    /**
     * Creates a new BulletEntity.
     *
     * @param position  The position of the bullet.
     * @param map       The map the bullet is in.
     * @param damage    The damage the bullet deals.
     * @param direction The direction the bullet is moving in.
     */
    public BulletEntity(Offset position, Map map, int damage, Offset direction) {
        super(position.add(direction), null, map);
        this.damage = damage;
        this.direction = direction;

        manager = map().getEntitiesOfType(BulletManagerEntity.class).getFirst();
        manager.addBullet(this);
    }

    @Override
    public Character representation() {
        return '⏺';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(true, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            playerEntity.damage(damage);
        }
    }

    public List<TurnStep> move() {
        return List.of(
            new MoveInDirection(this, direction),
            new DoIfLastStepFails(_ -> {
                destroy();
                return true;
            })
        );
    }

    @Override
    protected void dispose() {
        manager.removeBullet(this);
    }
}
