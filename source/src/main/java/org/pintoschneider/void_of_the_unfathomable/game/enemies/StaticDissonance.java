package org.pintoschneider.void_of_the_unfathomable.game.enemies;

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
}
