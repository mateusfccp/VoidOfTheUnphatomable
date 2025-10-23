package org.pintoschneider.void_of_the_unfathomable.game.enemies;

import org.pintoschneider.void_of_the_unfathomable.game.Player;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.DoIfLastStepSucceeds;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.MoveTowardsPlayer;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.RegularAttack;
import org.pintoschneider.void_of_the_unfathomable.game.turn_steps.TurnStep;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Static Dissonance enemy.
 * <p>
 * This enemy is the most basic enemy in the game. It has low health and attack power, but is quick to approach the
 * player, moving up to two tiles per turn, and is usually found in groups of three or more.
 * <p>
 * When idle, the Static Dissonance will keep its position, as if waiting for something to happen. However, if it spots
 * the player, it will immediately move towards them, closing the distance quickly.
 */
public final class StaticDissonance extends Enemy {
    public StaticDissonance() {
        super("Disonancia Estática", 10, 2, 1);
    }

    @Override
    public List<TurnStep> processTurn(Map.Entity<Enemy> entity) {
        // If the entity can see the player, it moves towards them
        final Map map = entity.map();
        final Map.Entity<Player> playerEntity = map.entitiesOfType(Player.class).getFirst();

        List<TurnStep> steps = new ArrayList<>();

        if (entity.canSee(playerEntity)) {
            final int distance = entity.distanceTo(playerEntity);

            switch (distance) {
                case 1 -> steps.add(new RegularAttack(entity));
                case 2 -> {
                    steps.add(new MoveTowardsPlayer(entity));
                    steps.add(
                        new DoIfLastStepSucceeds(
                            new RegularAttack(entity)
                        )
                    );
                }
                default -> {
                    steps.add(new MoveTowardsPlayer(entity));
                    steps.add(new MoveTowardsPlayer(entity));
                }
            }
        }

        return steps;
    }
}
