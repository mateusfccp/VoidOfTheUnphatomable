package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.TurretOfNothingness;
import org.pintoschneider.void_of_the_unfathomable.game.items.Item;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.FragmentOfNothingness;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.util.List;
import java.util.stream.IntStream;

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
    public Character representation() {
        return '█';
    }

    public Paint paint() {
        return new Paint().withForegroundColor(ColorPalette.CLAY);
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, true);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            final Player player = playerEntity.associatedObject();
            damage(player.attack(), player.hitChance());
        }
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
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.NORTH_WEST);
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.NORTH_EAST);
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.SOUTH_WEST);
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.SOUTH_EAST);
        } else {
            isShootingDiagonal = true;
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.NORTH);
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.SOUTH);
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.EAST);
            new BulletEntity(position(), map(), associatedObject().attack(), Offset.WEST);
        }

        return List.of();
    }

    @Override
    protected List<Item> loot() {
        final int dropQuantity = 2 + (int) (Math.random() * 4);

        return IntStream.range(0, dropQuantity)
            .<Item>mapToObj(_ -> new FragmentOfNothingness())
            .toList();
    }
}
