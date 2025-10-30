package org.pintoschneider.void_of_the_unfathomable.game.entities;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.enemies.StaticDissonance;
import org.pintoschneider.void_of_the_unfathomable.game.items.key_items.FragmentOfNothingness;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.SpatialProperty;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.DoIfLastStepSucceeds;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveTowardsPlayer;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.RegularAttack;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.ArrayList;
import java.util.List;

public final class StaticDissonanceEntity extends Entity<StaticDissonance> {
    public StaticDissonanceEntity(Offset position, Map map) {
        super(position, new StaticDissonance(), map);
    }

    @Override
    public char representation() {
        return '*';
    }

    @Override
    public SpatialProperty spatialProperty() {
        return new SpatialProperty(false, false);
    }

    @Override
    public void interact(Entity<?> entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            final Player player = playerEntity.associatedObject();
            this.associatedObject().damage(player.attack());

            if (this.associatedObject().health() == 0) {
                final int dropQuantity = 1 + (int) (Math.random() * 2);
                drop(new FragmentOfNothingness(), dropQuantity);
                destroy();
            }
        }
    }

    @Override
    public List<TurnStep> processTurn() {
        // If the entity can see the player, it moves towards them
        final Entity<Player> playerEntity = map().entitiesOfType(Player.class).getFirst();

        List<TurnStep> steps = new ArrayList<>();

        if (canSee(playerEntity)) {
            final int distance = distanceTo(playerEntity);

            switch (distance) {
                case 1 -> steps.add(new RegularAttack<>(this));
                case 2 -> {
                    steps.add(new MoveTowardsPlayer(this));
                    steps.add(
                        new DoIfLastStepSucceeds(
                            new RegularAttack<>(this)
                        )
                    );
                }
                default -> {
                    steps.add(new MoveTowardsPlayer(this));
                    steps.add(new MoveTowardsPlayer(this));
                }
            }
        }

        return steps;
    }
}
