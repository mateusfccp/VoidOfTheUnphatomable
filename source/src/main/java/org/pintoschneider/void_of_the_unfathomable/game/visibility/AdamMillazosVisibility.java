package org.pintoschneider.void_of_the_unfathomable.game.visibility;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.map.Map;

/**
 * The Adam Millazos visibility algorithm.
 * <p>
 * This algorithm is not as fast as other algorithms, but it produces very accurate results.
 * <p>
 * You can read more about it on <a href="https://www.adammil.net/blog/v125_Roguelike_Vision_Algorithms.html">this
 * article</a>.
 */
final public class AdamMillazosVisibility extends Visibility {
    final int rangeLimit = Math.max(map.width(), map.height());

    /**
     * Constructs a AdamMillazosVisibility instance for the given map.
     *
     * @param map The map on which visibility calculations will be performed.
     */
    public AdamMillazosVisibility(Map map) {
        super(map);
    }

    @Override
    boolean[][] compute(Offset origin) {
        final boolean[][] visibility = new boolean[map.width()][map.height()];

        // Origin is always visible
        visibility[origin.dx()][origin.dy()] = true;

        for (int octant = 0; octant < 8; octant++) {
            computeOctant(octant, origin, 1, new Slope(1, 1), new Slope(0, 1), visibility);
        }

        return visibility;
    }

    private void computeOctant(int octant, Offset origin, int x, Slope top, Slope bottom, boolean[][] visibility) {
        for (; x <= rangeLimit; x++) {
            int topY;
            if (top.x() == 1) {
                //noinspection SuspiciousNameCombination
                topY = x;
            } else {
                topY = ((x * 2 - 1) * top.y() + top.x()) / (top.x() * 2);

                if (blocksLight(x, topY, octant, origin)) {
                    if (top.greaterThanOrEqual(topY * 2 + 1, x * 2)
                        && !blocksLight(x, topY + 1, octant, origin)) topY = topY + 1;
                } else {
                    int ax = x * 2;

                    if (blocksLight(x + 1, topY + 1, octant, origin)) ax = ax + 1;

                    if (top.greaterThan(topY * 2 + 1, ax)) topY = topY + 1;
                }
            }

            int bottomY;
            if (bottom.y() == 0) {
                bottomY = 0;
            } else {
                bottomY = ((x * 2 - 1) * bottom.y() + bottom.x()) / (bottom.x() * 2);

                if (bottom.greaterThanOrEqual(bottomY * 2 + 1, x * 2)
                    && blocksLight(x, bottomY, octant, origin)
                    && !blocksLight(x, bottomY + 1, octant, origin)) {
                    bottomY = bottomY + 1;
                }
            }

            int wasOpaque = -1;
            for (int y = topY; y >= bottomY; y--) {
                boolean isOpaque = blocksLight(x, y, octant, origin);
                final Offset offset = new Offset(x, y);
                if (rangeLimit < 0 || origin.chebyshevDistanceTo(offset) <= rangeLimit) {
                    boolean isVisible = isOpaque
                        || (y != topY || top.greaterThan(y * 4 - 1, x * 4 + 1))
                        && (y != bottomY || bottom.lessThan(y * 4 + 1, x * 4 - 1));

                    if (isVisible) setVisible(x, y, octant, origin, visibility);

                    if (x != rangeLimit) {
                        if (isOpaque) {
                            if (wasOpaque == 0) {
                                int nx = x * 2;
                                int ny = y * 2 + 1;

                                if (top.greaterThan(ny, nx)) {
                                    if (y == bottomY) {
                                        bottom = new Slope(ny, nx);
                                        break;
                                    } else {
                                        computeOctant(octant, origin, x + 1, top, new Slope(ny, nx), visibility);
                                    }
                                } else {
                                    if (y == bottomY) return;
                                }
                            }
                            wasOpaque = 1;
                        } else {
                            if (wasOpaque > 0) {
                                int nx = x * 2;
                                int ny = y * 2 + 1;

                                if (bottom.greaterThanOrEqual(ny, nx)) {
                                    return;
                                }

                                top = new Slope(ny, nx);
                            }

                            wasOpaque = 0;
                        }
                    }
                }
            }

            if (wasOpaque != 0) break;
        }
    }

    /**
     * Transforms local (x, y) coordinates in an octant to map coordinates.
     */
    private Offset transformOctant(int x, int y, int octant, Offset origin) {
        int nx = origin.dx();
        int ny = origin.dy();

        switch (octant) {
            case 0 -> {
                nx += x;
                ny -= y;
            }
            case 1 -> {
                nx += y;
                ny -= x;
            }
            case 2 -> {
                nx -= y;
                ny -= x;
            }
            case 3 -> {
                nx -= x;
                ny -= y;
            }
            case 4 -> {
                nx -= x;
                ny += y;
            }
            case 5 -> {
                nx -= y;
                ny += x;
            }
            case 6 -> {
                nx += y;
                ny += x;
            }
            case 7 -> {
                nx += x;
                ny += y;
            }
        }
        return new Offset(nx, ny);
    }

    boolean blocksLight(int x, int y, int octant, Offset origin) {
        final Offset position = transformOctant(x, y, octant, origin);

        if (position.dx() < 0 || position.dx() >= map.width() ||
            position.dy() < 0 || position.dy() >= map.height()) {
            return false;
        }

        return blocksLight(position);
    }

    void setVisible(int x, int y, int octant, Offset origin, boolean[][] visibility) {
        final Offset position = transformOctant(x, y, octant, origin);

        if (position.dx() < 0 || position.dx() >= map.width() ||
            position.dy() < 0 || position.dy() >= map.height()) {
            return;
        }

        visibility[position.dx()][position.dy()] = true;
    }
}

record Slope(int y, int x) {
    boolean greaterThan(int otherY, int otherX) {
        return this.y * otherX > this.x * otherY;
    }

    boolean greaterThanOrEqual(int otherY, int otherX) {
        return this.y * otherX >= this.x * otherY;
    }

    boolean lessThan(int otherY, int otherX) {
        return this.y * otherX < this.x * otherY;
    }
}