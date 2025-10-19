package org.pintoschneider.void_of_the_unfathomable.game.visibility;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

import java.util.HashMap;

/**
 * A {@link Visibility} implementation that uses a ray-casting algorithm to determine visibility.
 * <p>
 * This implementation simulates reasonable visibility by casting rays from the origin to the target using the
 * Bresenham's line algorithm. It checks for obstacles along the path to determine if the target is visible.
 * <p>
 * This method is simple and fast, it often produces a lot of artifacts, and is not symmetric (i.e., visibility from A
 * to B may differ from visibility from B to A).
 */
public final class RayCastingVisibility extends Visibility {
    public RayCastingVisibility(Map map) {
        super(map);
    }

    boolean[][] compute(Offset origin) {
        final boolean[][] visibility = new boolean[map.width()][map.height()];

        // Origin is always visible
        visibility[origin.dx()][origin.dy()] = true;

        for (int x = 0; x < map.width(); x++) {
            computeRay(origin, new Offset(x, 0), visibility);
            computeRay(origin, new Offset(x, map.height() - 1), visibility);
        }

        for (int y = 0; y < map.height(); y++) {
            computeRay(origin, new Offset(0, y), visibility);
            computeRay(origin, new Offset(map.width() - 1, y), visibility);
        }

        return visibility;
    }

    void computeRay(Offset origin, Offset target, boolean[][] visibility) {
        final int dx = Math.abs(target.dx() - origin.dx());
        final int dy = Math.abs(target.dy() - origin.dy());
        final int sx = origin.dx() < target.dx() ? 1 : -1;
        final int sy = origin.dy() < target.dy() ? 1 : -1;

        final int longAxisStep;
        final int shortAxisStep;
        int longAxisPosition;
        int shortAxisPosition;
        int longAxisLength;
        int shortAxisLength;

        if (dx >= dy) {
            longAxisLength = dx;
            shortAxisLength = dy;
            longAxisPosition = origin.dx();
            shortAxisPosition = origin.dy();
            longAxisStep = sx;
            shortAxisStep = sy;
        } else {
            longAxisLength = dy;
            shortAxisLength = dx;
            longAxisPosition = origin.dy();
            shortAxisPosition = origin.dx();
            longAxisStep = sy;
            shortAxisStep = sx;
        }

        final int errorStep = shortAxisLength * 2;
        final int errorReset = longAxisLength * 2;

        int error = -longAxisLength;

        // Skip the first point (the origin)
        while (--longAxisLength >= 0) {
            longAxisPosition = longAxisPosition + longAxisStep;
            error = error + errorStep;
            if (error >= 0) {
                shortAxisPosition = shortAxisPosition + shortAxisStep;
                error = error - errorReset;
            }

            final Offset currentPosition = dx >= dy
                ? new Offset(longAxisPosition, shortAxisPosition)
                : new Offset(shortAxisPosition, longAxisPosition);

            visibility[currentPosition.dx()][currentPosition.dy()] = true;

            if (blocksLight(currentPosition)) {
                break;
            }
        }
    }
}
