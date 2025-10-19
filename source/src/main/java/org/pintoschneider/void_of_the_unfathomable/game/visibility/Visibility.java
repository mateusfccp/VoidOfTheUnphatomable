package org.pintoschneider.void_of_the_unfathomable.game.visibility;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;
import org.pintoschneider.void_of_the_unfathomable.game.map.MapTile;

import java.util.HashMap;

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
        this.map = map;
    }

    public boolean isVisible(Offset origin, Offset target) {
        if (!visibilityCache.containsKey(origin)) {
            final boolean[][] visibility = compute(origin);
            visibilityCache.put(origin, visibility);
        }

        final boolean[][] visibility = visibilityCache.get(origin);

        return visibility[target.dx()][target.dy()];
    }

    abstract boolean[][] compute(Offset origin);

    boolean blocksLight(Offset position) {
        final MapTile tile = map.getTileAt(position);

        if (tile == null) {
            return true;
        } else {
            return tile.opaque();
        }
    }
}

