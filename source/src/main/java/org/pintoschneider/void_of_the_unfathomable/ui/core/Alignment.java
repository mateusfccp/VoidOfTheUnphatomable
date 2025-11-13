package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.core.Size;

/**
 * A point within a rectangle.
 * <p>
 * {@code Alignment(0.0, 0.0)} represents the center of the rectangle. The distance from -1.0 to +1.0 is the distance
 * from one side of the rectangle to the other side of the rectangle. Therefore, 2.0 units horizontally (or vertically)
 * is equivalent to the width (or height) of the rectangle.
 * <p>
 * {@code Alignment(-1.0, -1.0)} represents the top left of the rectangle.
 * <p>
 * {@code Alignment(1.0, 1.0)} represents the bottom right of the rectangle.
 * <p>
 * {@code Alignment(0.0, 3.0)} represents a point that is horizontally centered with respect to the rectangle and
 * vertically below the bottom of the rectangle by the height of the rectangle.
 * <p>
 * {@code Alignment(0.0, -0.5)} represents a point that is horizontally centered with respect to the rectangle and
 * vertically halfway between the top edge and the center.
 * <p>
 * {@code Alignment(x, y)} in a rectangle with height h and width w describes the point (x * w/2 + w/2, y * h/2 + h/2)
 * in the coordinate system of the rectangle.
 *
 * @param x The x value of the alignment, ranging from -1.0 (left) to 1.0 (right).
 * @param y The y value of the alignment, ranging from -1.0 (top) to 1.0 (bottom).
 */
public record Alignment(double x, double y) {
    /**
     * Alignment in the top-left corner of a rectangle.
     */
    public static final Alignment TOP_LEFT = new Alignment(-1.0, -1.0);
    /**
     * Alignment in the top-center of a rectangle.
     */
    public static final Alignment TOP_CENTER = new Alignment(0.0, -1.0);
    /**
     * Alignment in the top-right corner of a rectangle.
     */
    public static final Alignment TOP_RIGHT = new Alignment(1.0, -1.0);
    /**
     * Alignment in the center-left of a rectangle.
     */
    public static final Alignment CENTER_LEFT = new Alignment(-1.0, 0.0);
    /**
     * Alignment in the center of a rectangle.
     */
    public static final Alignment CENTER = new Alignment(0.0, 0.0);
    /**
     * Alignment in the center-right of a rectangle.
     */
    public static final Alignment CENTER_RIGHT = new Alignment(1.0, 0.0);
    /**
     * Alignment in the bottom-left corner of a rectangle.
     */
    public static final Alignment BOTTOM_LEFT = new Alignment(-1.0, 1.0);
    /**
     * Alignment in the bottom-center of a rectangle.
     */
    public static final Alignment BOTTOM_CENTER = new Alignment(0.0, 1.0);
    /**
     * Alignment in the bottom-right corner of a rectangle.
     */
    public static final Alignment BOTTOM_RIGHT = new Alignment(1.0, 1.0);

    /**
     * Creates an alignment with the given x and y values.
     *
     * @param x The x value of the alignment, ranging from -1.0 (left) to 1.0 (right).
     * @param y The y value of the alignment, ranging from -1.0 (top) to 1.0 (bottom).
     */
    public Alignment {
    }

    /**
     * Computes the offset within a given size that corresponds to this alignment.
     *
     * @param other The size of the rectangle.
     * @return The {@link Offset} representing the aligned point within the rectangle.
     */
    public Offset alongSize(Size other) {
        final double centerX = other.width() / 2.0;
        final double centerY = other.height() / 2.0;
        final int dx = (int) Math.round(centerX + x * centerX);
        final int dy = (int) Math.round(centerY + y * centerY);

        return new Offset(dx, dy);
    }
}
