package org.pintoschneider.void_of_the_unfathomable.ui;

/**
 * A class representing a cartesian size, with width and height.
 */
public class Size {
    private final int width;
    private final int height;

    /**
     * Constructs a Size object with the specified width and height.
     *
     * @param width  the width of the size
     * @param height the height of the size
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width of the size.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the size.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }
}
