package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.pintoschneider.void_of_the_unfathomable.core.Size;

/**
 * A class representing layout constraints with minimum and maximum width and height.
 */
public final class Constraints {
    final private int minWidth;
    final private int minHeight;
    final private int maxWidth;
    final private int maxHeight;

    /**
     * Constructs a Constraints object with the specified minimum and maximum width and height.
     * <p>
     * Null values are treated as unbounded (0 for minimums and Integer.MAX_VALUE for maximums).
     *
     * @param minWidth  The minimum width constraint (nullable).
     * @param maxWidth  The maximum width constraint (nullable).
     * @param minHeight The minimum height constraint (nullable).
     * @param maxHeight The maximum height constraint (nullable).
     * @throws IllegalArgumentException if any of the constraints are negative or if minimums are greater than maximums.
     */
    public Constraints(Integer minWidth, Integer maxWidth, Integer minHeight, Integer maxHeight) {
        if (minWidth == null) minWidth = 0;
        if (maxWidth == null) maxWidth = Integer.MAX_VALUE;
        if (minHeight == null) minHeight = 0;
        if (maxHeight == null) maxHeight = Integer.MAX_VALUE;

        if (minWidth < 0 || maxWidth < 0 || minHeight < 0 || maxHeight < 0) {
            throw new IllegalArgumentException("Constraints values must be non-negative.");
        }

        if (minWidth > maxWidth) {
            throw new IllegalArgumentException("Minimum width cannot be greater than maximum width.");
        }

        if (minHeight > maxHeight) {
            throw new IllegalArgumentException("Minimum height cannot be greater than maximum height.");
        }

        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    /**
     * Creates a Constraints object with tight constraints.
     *
     * @param width  The exact width constraint.
     * @param height The exact height constraint.
     * @return A {@link Constraints} object with tight constraints.
     */
    public static Constraints tight(int width, int height) {
        return new Constraints(width, width, height, height);
    }

    /**
     * Creates a Constraints object with tight constraints from a Size object.
     *
     * @param size The exact size constraint.
     * @return A {@link Constraints} object with tight constraints.
     */
    public static Constraints tight(Size size) {
        return new Constraints(size.width(), size.width(), size.height(), size.height());
    }


    /**
     * Creates a Constraints object with loose constraints.
     *
     * @param width  The maximum width constraint.
     * @param height The maximum height constraint.
     * @return A {@link Constraints} object with loose constraints.
     */
    public static Constraints loose(int width, int height) {
        return new Constraints(0, width, 0, height);
    }

    /**
     * Creates a Constraints object with loose constraints from a Size object.
     *
     * @param size The maximum size constraint.
     * @return A {@link Constraints} object with loose constraints.
     */
    public static Constraints loose(Size size) {
        return new Constraints(0, size.width(), 0, size.height());
    }

    /**
     * Creates a Constraints object that forces expansion in both width and height.
     *
     * @return A {@link Constraints} object with expanded constraints.
     */
    public static Constraints expand() {
        return new Constraints(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * The minimum width constraint.
     *
     * @return The minimum width constraint.
     */
    public int minWidth() {
        return minWidth;
    }

    /**
     * The maximum width constraint.
     *
     * @return The maximum width constraint.
     */
    public int maxWidth() {
        return maxWidth;
    }

    /**
     * The minimum height constraint.
     *
     * @return The minimum height constraint.
     */
    public int minHeight() {
        return minHeight;
    }

    /**
     * The maximum height constraint.
     *
     * @return The maximum height constraint.
     */
    public int maxHeight() {
        return maxHeight;
    }

    /**
     * Checks if the constraints are tight (i.e., min and max dimensions are equal).
     *
     * @return true if the constraints are tight, false otherwise.
     */
    public boolean isTight() {
        return hasTightWidth() && hasTightHeight();
    }

    /**
     * Checks if the width constraints are tight (i.e., minimum width is equals to maximum width).
     *
     * @return true if the width constraints are tight, false otherwise.
     */
    public boolean hasTightWidth() {
        return minWidth == maxWidth;
    }

    /**
     * Checks if the height constraints are tight (i.e., minimum height is equals to maximum height).
     *
     * @return true if the height constraints are tight, false otherwise.
     */
    public boolean hasTightHeight() {
        return minHeight == maxHeight;
    }

    /**
     * Returns a {@link Size} object representing the biggest size that satisfies the constraints.
     *
     * @return A {@link Size} object with the maximum width and height.
     */
    public Size biggest() {
        return new Size(maxWidth, maxHeight);
    }

    /**
     * Returns a {@link Size} object representing the smallest size that satisfies the constraints.
     *
     * @return A {@link Size} object with the minimum width and height.
     */
    public Size smallest() {
        return new Size(minWidth, minHeight);
    }

    /**
     * Constrains the given size to fit within the constraints.
     *
     * @param size The size to be constrained.
     * @return A new Size object that fits within the constraints.
     */
    public Size constrain(Size size) {
        final int constrainedWidth = Math.clamp(size.width(), minWidth, maxWidth);
        final int constrainedHeight = Math.clamp(size.height(), minHeight, maxHeight);
        return new Size(constrainedWidth, constrainedHeight);
    }

    /**
     * Returns new constraints that respect the given constraints while being as close as possible to the original
     * constraints.
     *
     * @param constraints The constraints to be enforced.
     * @return A new {@link Constraints} object that respects the given constraints.
     */
    public Constraints enforce(Constraints constraints) {
        return new Constraints(
            Math.clamp(minWidth, constraints.minWidth, constraints.maxWidth),
            Math.clamp(maxWidth, constraints.minWidth, constraints.maxWidth),
            Math.clamp(minHeight, constraints.minHeight, constraints.maxHeight),
            Math.clamp(maxHeight, constraints.minHeight, constraints.maxHeight)
        );
    }

    /**
     * Returns new constraints that are inflated by the given amounts in width and height.
     * <p>
     * Only {@link #maxWidth} and {@link #maxHeight} are affected.
     *
     * @param width  The amount to enlarge the height constraints.
     * @param height The amount to enlarge the width constraints.
     * @return A new {@link Constraints} object that is inflated by the given amounts.
     */
    public Constraints inflate(int width, int height) {
        return new Constraints(
            Math.max(0, minWidth),
            Math.max(0, maxWidth + width),
            Math.max(0, minHeight),
            Math.max(0, maxHeight + height)
        );
    }

    /**
     * Returns new constraints that are inflated by the given insets.
     *
     * @param insets The insets to inflate the constraints by.
     * @return A new {@link Constraints} object that is inflated by the given {@code insets}.
     */
    public Constraints inflate(EdgeInsets insets) {
        return inflate(insets.horizontal(), insets.vertical());
    }

    /**
     * Returns new constraints that are deflated by the given amounts in width and height.
     * <p>
     * The minimum constraints will never be less than 0, and the maximum constraints will never be less than the
     * minimum constraints.
     *
     * @param width  The amount to deflate the height constraints.
     * @param height The amount to deflate the width constraints.
     * @return A new {@link Constraints} object that is deflated by the given amounts.
     */
    public Constraints deflate(int width, int height) {
        final int deflatedMinWidth = Math.max(0, minWidth - width);
        final int deflatedMinHeight = Math.max(0, minHeight - height);

        return new Constraints(
            deflatedMinWidth,
            Math.max(deflatedMinWidth, maxWidth - width),
            deflatedMinHeight,
            Math.max(deflatedMinHeight, maxHeight - width)
        );
    }

    /**
     * Returns new constraints that are deflated by the given insets.
     *
     * @param insets The insets to deflate the constraints by.
     * @return A new {@link Constraints} object that is deflated by the given {@code insets}.
     */
    public Constraints deflate(EdgeInsets insets) {
        return deflate(insets.horizontal(), insets.vertical());
    }

    /**
     * Returns new tight constraints based on the current maximum width and height.
     *
     * @return A new {@link Constraints} object that is tight.
     */
    public Constraints tighten() {
        return new Constraints(maxWidth, maxWidth, maxHeight, maxHeight);
    }

    /**
     * Returns new loose constraints based on the current maximum width and height.
     *
     * @return A new {@link Constraints} object that is loose.
     */
    public Constraints loosen() {
        return new Constraints(0, maxWidth, 0, maxHeight);
    }

    @Override
    public String toString() {
        return "Constraints[" +
            "minWidth=" + minWidth + ", " +
            "maxWidth=" + maxWidth + ", " +
            "minHeight=" + minHeight + ", " +
            "maxHeight=" + maxHeight + ']';
    }

}
