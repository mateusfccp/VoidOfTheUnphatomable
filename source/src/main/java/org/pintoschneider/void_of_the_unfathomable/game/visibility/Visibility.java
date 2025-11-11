package org.pintoschneider.void_of_the_unfathomable.game.visibility;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.entities.Entity;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapTile;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * An interface defining the visibility rules on a map.
 * <p>
 * Implementations of this interface determine whether a target position is visible from an origin position. This is
 * used both for rendering purposes and for gameplay mechanics, such as line of sight.
 */
public abstract class Visibility {
    final Map map;
    private final HashMap<Offset, boolean[][]> visibilityCache = new HashMap<>();

    protected Visibility(Map map) {
        this.map = Objects.requireNonNull(map);
    }

    /**
     * Determines if the target position is visible from the origin position.
     *
     * @param origin The starting position.
     * @param target The target position to check visibility for.
     * @return True if the target is visible from the origin, false otherwise.
     */
    public boolean isVisible(Offset origin, Offset target) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(target);

        if (!visibilityCache.containsKey(origin)) {
            final boolean[][] visibility = compute(origin);
            visibilityCache.put(origin, visibility);
        }

        final boolean[][] visibility = visibilityCache.get(origin);

        return visibility[target.dx()][target.dy()];
    }

    abstract boolean[][] compute(Offset origin);

    protected boolean blocksLight(Offset position) {
        return map.opaque(position);
    }
}

