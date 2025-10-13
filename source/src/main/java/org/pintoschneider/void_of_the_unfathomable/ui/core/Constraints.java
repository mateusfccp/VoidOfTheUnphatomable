package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A class representing layout constraints with minimum and maximum width and height.
 */
public record Constraints(int minWidth, int maxWidth, int minHeight, int maxHeight) {
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
}
