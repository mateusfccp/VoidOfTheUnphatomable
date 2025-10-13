package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * An interface for objects that can be drawn on the screen.
 */
public abstract class Drawable {
    protected Size size;

    /**
     * Lays out the drawable within the given constraints and their children.
     * <p>
     * Within this method, the drawable should determine its size based on the provided constraints, and should call
     * layout on any child drawables with appropriate constraints.
     *
     * @param constraints The constraints within which the drawable must fit.
     */
    public abstract void layout(Constraints constraints);

    /**
     * Draws the drawable onto the provided canvas.
     * <p>
     * This method is called after layout has been performed, and the drawable should use the canvas to render itself
     * and any child drawables.
     *
     * @param canvas The canvas to draw on.
     */
    public abstract void draw(Canvas canvas);

    /**
     * Returns the size of the drawable after layout has been performed.
     * <p>
     * If layout has not been called yet, the size may be null or undefined, which will throw an exception.
     *
     * @return The size of the drawable.
     */
    public Size getSize() {
        return size;
    }
}

