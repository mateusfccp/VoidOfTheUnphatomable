package org.pintoschneider.void_of_the_unfathomable.game;

/**
 * Enum representing various status effects that can affect a player or monster.
 */
public enum StatusEffect {
    /**
     * The entity is weakened and has reduced attack power.
     */
    DEPRESSION("Depresión"),

    /**
     * The entity is addicted to a substance.
     */
    DEPENDENCY("Adicción"),

    /**
     * The entity is experiencing withdrawal symptoms from stopping a medication.
     */
    DISCONTINUATION_SYNDROME("Síndrome de Discontinuación"),

    /**
     * The entity is experiencing involuntary muscle spasms.
     */
    DYSKINESIA("Discinesia"),

    /**
     * The entity has severe involuntary movements.
     */
    TARDIVE_DYSKINESIA("Discinesia Tardía"),

    /**
     * The entity is insane and has unpredictable behavior.
     */
    INSANITY("Insanidad"),

    /**
     * The entity is dead.
     */
    DEATH("Muerte");

    final String displayString;

    StatusEffect(String displayString) {
        this.displayString = displayString;
    }

    /**
     * Gets the name of the status effect.
     *
     * @return The name of the status effect.
     */
    public String displayString() {
        return displayString;
    }
}
