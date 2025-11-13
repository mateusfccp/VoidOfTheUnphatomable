package org.pintoschneider.void_of_the_unfathomable.game.enemies;

/**
 * A class representing the Mimic enemy.
 * <p>
 * The mimic is a deceptive enemy that disguises itself as a treasure chest to lure unsuspecting players.
 * It has moderate health and defense, making it a formidable opponent when revealed.
 * <p>
 * It doesn't move until the player tries to interact with it, at which point it follows the player for eternity.
 */
public final class Mimic extends Enemy {
    private State state = State.IDLE;

    /**
     * Creates a new Mimic enemy with predefined attributes.
     */
    public Mimic() {
        super("Mímico", 20, 5, 3);
    }

    /**
     * Gets the current state of the Mimic.
     *
     * @return The current state of the Mimic.
     */
    public State state() {
        return state;
    }

    /**
     * Sets the Mimic to attack the player.
     */
    public void attackPlayer() {
        this.state = State.ATTACKING;
    }

    /**
     * Sets the Mimic to follow the player.
     */
    public void followPlayer() {
        this.state = State.FOLLOWING_PLAYER;
    }

    /**
     * The different states a Mimic can be in.
     */
    public enum State {
        /**
         * The Mimic is idle, pretending to be a treasure chest.
         */
        IDLE,

        /**
         * The Mimic is following the player.
         */
        FOLLOWING_PLAYER,

        /**
         * The Mimic is attacking the player.
         */
        ATTACKING
    }
}