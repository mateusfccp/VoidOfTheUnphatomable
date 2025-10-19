package org.pintoschneider.void_of_the_unfathomable.core;

/**
 * An immutable 2D integer offset.
 *
 * @param dx The horizontal offset.
 * @param dy The vertical offset.
 */
public record Offset(int dx, int dy) {
    /**
     * A constant representing an offset of (0, 0).
     */
    public static final Offset ZERO = new Offset(0, 0);

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
}
