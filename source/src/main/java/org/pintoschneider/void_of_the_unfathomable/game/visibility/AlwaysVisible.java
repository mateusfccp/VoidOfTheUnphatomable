package org.pintoschneider.void_of_the_unfathomable.game.visibility;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

/**
 * A {@link Visibility} implementation that makes all positions visible from any origin.
 * <p>
 * This should be used for debugging purposes only, as it effectively disables any visibility restrictions, making the
 * enemies able to see the player at all times.
 */
public final class AlwaysVisible extends Visibility {
    /**
     * Creates a new {@link AlwaysVisible} visibility rule for the given map.
     *
     * @param map The map to apply the visibility rule to.
     */
    public AlwaysVisible(Map map) {
        super(map);
    }

    @Override
    public boolean isVisible(Offset origin, Offset target) {
        return true;
    }

    @Override
    boolean[][] compute(Offset origin) {
        return null;
    }
}
