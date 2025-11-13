package org.pintoschneider.void_of_the_unfathomable.core;

import java.util.Objects;

/**
 * An immutable 2D integer offset.
 *
 * @param dx The horizontal offset.
 * @param dy The vertical offset.
 */
public record Offset(int dx, int dy) {
    /**
     * A constant that represents an offset of (0, 0).
     */
    public static final Offset ZERO = new Offset(0, 0);

    /**
     * A constant that represents an offset of (-1, 0).
     */
    public static final Offset WEST = new Offset(-1, 0);

    /**
     * A constant that represents an offset of (1, 0).
     */
    public static final Offset EAST = new Offset(1, 0);

    /**
     * A constant that represents an offset of (0, -1).
     */
    public static final Offset NORTH = new Offset(0, -1);

    /**
     * A constant that represents an offset of (0, 1).
     */
    public static final Offset SOUTH = new Offset(0, 1);

    /**
     * A constant that represents an offset of (-1, -1).
     */
    public static final Offset NORTH_WEST = new Offset(-1, -1);

    /**
     * A constant that represents an offset of (1, -1).
     */
    public static final Offset NORTH_EAST = new Offset(1, -1);

    /**
     * A constant that represents an offset of (-1, 1).
     */
    public static final Offset SOUTH_WEST = new Offset(-1, 1);

    /**
     * A constant that represents an offset of (1, 1).
     */
    public static final Offset SOUTH_EAST = new Offset(1, 1);

    /**
     * Returns a new Offset that is the sum of this offset and another.
     *
     * @param other The other offset to add.
     * @return The resulting Offset after addition.
     */
    public Offset add(Offset other) {
        return new Offset(this.dx + other.dx, this.dy + other.dy);
    }

    /**
     * Returns a new Offset that is the difference between this offset and another.
     *
     * @param other The other offset to subtract.
     * @return The resulting Offset after subtraction.
     */
    public Offset subtract(Offset other) {
        return new Offset(this.dx - other.dx, this.dy - other.dy);
    }

    /**
     * Returns a new Offset that is this offset scaled by a factor.
     *
     * @param factor The factor to multiply by.
     * @return The resulting Offset after multiplication.
     */
    public Offset multiply(int factor) {
        return new Offset(this.dx * factor, this.dy * factor);
    }

    /**
     * Returns a new Offset that is this offset divided by a divisor.
     *
     * @param divisor The divisor to divide by.
     * @return The resulting Offset after division.
     */
    public Offset divide(int divisor) {
        return new Offset(this.dx / divisor, this.dy / divisor);
    }

    /**
     * Returns a new Offset that is this offset translated by given deltas.
     *
     * @param dx The horizontal delta to translate by.
     * @param dy The vertical delta to translate by.
     * @return The resulting Offset after translation.
     */
    public Offset translate(int dx, int dy) {
        return new Offset(this.dx + dx, this.dy + dy);
    }

    /**
     * Calculates the Chebyshev distance to another offset.
     * <p>
     * The Chebyshev distance is defined as the maximum of the absolute differences in each dimension. It is used in
     * scenarios where movement can occur in any direction, including diagonally.
     *
     * @param to The target offset.
     * @return The Chebyshev distance to the target offset.
     */
    public int chebyshevDistanceTo(Offset to) {
        final int dx = Math.abs(this.dx - to.dx);
        final int dy = Math.abs(this.dy - to.dy);
        return Math.max(dx, dy);
    }

    /**
     * Calculates the Manhattan distance to another offset.
     * <p>
     * The Manhattan distance is defined as the sum of the absolute differences in each dimension. It is used in
     * scenarios where movement is restricted to horizontal and vertical directions.
     *
     * @param to The target offset.
     * @return The Manhattan distance to the target offset.
     */
    public int manhattanDistanceTo(Offset to) {
        final int dx = Math.abs(this.dx - to.dx);
        final int dy = Math.abs(this.dy - to.dy);
        return dx + dy;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Offset(int dx1, int dy1)) {
            return dx() == dx1 && dy() == dy1;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(dx(), dy());
    }
}
