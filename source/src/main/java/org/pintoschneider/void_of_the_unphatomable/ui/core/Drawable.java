package org.pintoschneider.void_of_the_unphatomable.ui.core;

import org.pintoschneider.void_of_the_unphatomable.ui.Size;

/**
 * An interface for objects that can be drawn on the screen.
 */
public abstract class Drawable {
    protected Size size;

    public abstract void layout(Size maximumSize);

    public abstract Character draw(int x, int y);

    public Size getSize() {
        return size;
    }
}

