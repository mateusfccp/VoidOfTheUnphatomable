package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A class that represents the amount of space to inset a component from its parent's edges.
 * <p>
 * It is defined by four values: top, right, bottom, and left.
 */
public final class EdgeInsets {
    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    /**
     * Creates an inset with the given top, right, bottom, and left values.
     *
     * @param top    The inset from the top edge.
     * @param right  The inset from the right edge.
     * @param bottom The inset from the bottom edge.
     * @param left   The inset from the left edge.
     */
    public EdgeInsets(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * Creates an inset with the given horizontal and vertical values.
     *
     * @param horizontal The inset from the left and right edges.
     * @param vertical   The inset from the top and bottom edges.
     */
    public EdgeInsets(int horizontal, int vertical) {
        this(horizontal, horizontal, vertical, vertical);
    }

    /**
     * Creates an inset with the same value for all edges.
     *
     * @param all The inset from all edges.
     */
    public EdgeInsets(int all) {
        this(all, all, all, all);
    }

    /**
     * Returns the inset from the top edge.
     *
     * @return The inset from the top edge.
     */
    public int top() {
        return top;
    }

    /**
     * Returns the inset from the right edge.
     *
     * @return The inset from the right edge.
     */
    public int right() {
        return right;
    }

    /**
     * Returns the inset from the bottom edge.
     *
     * @return The inset from the bottom edge.
     */
    public int bottom() {
        return bottom;
    }

    /**
     * Returns the inset from the left edge.
     *
     * @return The inset from the left edge.
     */
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
