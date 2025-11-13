package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A class that represents the amount of space to inset a component from its parent's edges.
 * <p>
 * It is defined by four values: top, right, bottom, and left.
 *
 * @param top    The inset from the top edge.
 * @param right  The inset from the right edge.
 * @param bottom The inset from the bottom edge.
 * @param left   The inset from the left edge.
 */
public record EdgeInsets(int top, int right, int bottom, int left) {
    /**
     * Creates an inset with the given horizontal and vertical values.
     *
     * @param horizontal The inset from the left and right edges.
     * @param vertical   The inset from the top and bottom edges.
     * @return The created EdgeInsets.
     */
    public static EdgeInsets symmetric(int horizontal, int vertical) {
        return new EdgeInsets(horizontal, vertical, horizontal, vertical);
    }

    /**
     * Creates an inset with the same value for all edges.
     *
     * @param value The inset from all edges.
     * @return The created EdgeInsets.
     */
    public static EdgeInsets all(int value) {
        return new EdgeInsets(value, value, value, value);
    }

    /**
     * Returns the inset from the top edge.
     *
     * @return The inset from the top edge.
     */
    @Override
    public int top() {
        return top;
    }

    /**
     * Returns the inset from the right edge.
     *
     * @return The inset from the right edge.
     */
    @Override
    public int right() {
        return right;
    }

    /**
     * Returns the inset from the bottom edge.
     *
     * @return The inset from the bottom edge.
     */
    @Override
    public int bottom() {
        return bottom;
    }

    /**
     * Returns the inset from the left edge.
     *
     * @return The inset from the left edge.
     */
    @Override
    public int left() {
        return left;
    }

    /**
     * Returns the total horizontal inset (left + right).
     *
     * @return The total horizontal inset.
     */
    public int horizontal() {
        return left + right;
    }

    /**
     * Returns the total vertical inset (top + bottom).
     *
     * @return The total vertical inset.
     */
    public int vertical() {
        return top + bottom;
    }
}
