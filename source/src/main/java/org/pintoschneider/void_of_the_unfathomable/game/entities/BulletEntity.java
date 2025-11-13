package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapTile;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.DoIfLastStepFails;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveInDirection;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.List;

/**
 * An entity representing a bullet in the game.
 */
public class BulletEntity extends Entity<Void> {
    final BulletManagerEntity manager;
    final private int damage;
    final private Offset direction;

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
            destroy();
        }
    }

    /**
     * Creates the turn steps for moving the bullet.
     *
     * @return The list of turn steps to be executed.
     */
    public List<TurnStep> move() {
        return List.of(
            new MoveInDirection(this, direction),
            new DoIfLastStepFails(_ -> {
                destroy();
                return true;
            })
        );
    }

    // We override it because a bullet should only be stopped by walls, not by other entities.
    // This is ugly, but works for now.
    @Override
    public boolean moveBy(Offset offset) {
        final Offset targetPosition = position().translate(offset.dx(), offset.dy());

        final boolean didMove;

        final List<Entity<?>> entitiesToInteract;

        final MapTile targetTile = map().getTileAt(targetPosition);

        if (targetTile != null && targetTile != MapTile.WALL && targetTile != MapTile.DENSE_VOID) {
            position = targetPosition;
            didMove = true;
            entitiesToInteract = map().getEntitiesAt(position);
        } else {
            didMove = false;
            entitiesToInteract = map().getEntitiesAt(targetPosition);
        }

        for (Entity<?> entity : entitiesToInteract) {
            if (entity != this) {
                entity.interact(this);
                this.interact(entity);
            }
        }

        return didMove;
    }

    @Override
    protected void dispose() {
        manager.removeBullet(this);
    }
}
