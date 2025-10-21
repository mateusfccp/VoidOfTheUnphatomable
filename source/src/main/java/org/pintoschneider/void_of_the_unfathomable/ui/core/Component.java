package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.pintoschneider.void_of_the_unfathomable.core.Size;

/**
 * An interface for objects that can be drawn on the screen.
 */
public abstract class Component {
    protected Size size;
    protected Object data;

    /**
     * Lays out the component within the given constraints and their children.
     * <p>
     * Within this method, the component should determine its size based on the provided constraints, and should call
     * layout on any child components with appropriate constraints.
     *
     * @param constraints The constraints within which the component must fit.
     */
    public abstract void layout(Constraints constraints);

    /**
     * Draws the component onto the provided canvas.
     * <p>
     * This method is called after layout has been performed, and the component should use the canvas to render itself
     * and any child components.
     *
     * @param canvas The canvas to draw on.
     */
    public abstract void draw(Canvas canvas);

    /**
     * Returns the size of the component after layout has been performed.
     * <p>
     * If layout has not been called yet, the size may be null or undefined, which will throw an exception.
     *
     * @return The size of the component.
     */
    public Size size() {
        return size;
    }

    /**
     * Returns the data associated with this component.
     *
     * @return The data associated with this component.
     */
    public Object data() {
        return data;
    }
}

