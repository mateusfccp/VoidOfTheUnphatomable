package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Drawable;
import org.pintoschneider.void_of_the_unfathomable.ui.Size;

/**
 * A bordered box that can contain a single {@link Drawable} child.
 */
public final class Box extends Drawable {
    private final Drawable child;

    /**
     * Constructs an empty Box with no child.
     */
    public Box() {
        this.child = null;
    }

    /**
     * Constructs a Box with the given child.
     *
     * @param child the child Drawable to be contained within the box
     */
    public Box(Drawable child) {
        this.child = child;
    }

    @Override
    public void layout(Size maximumSize) {
        size = maximumSize;

        if (child != null) {
            child.layout(new Size(size.getWidth() - 2, size.getHeight() - 2));
        }
    }

    @Override
    public Character draw(int x, int y) {
        if (x == 0) {
            if (y == 0) {
                return '┌';
            } else if (y == size.getHeight() - 1) {
                return '└';
            } else {
                return '│';
            }
        } else if (x == size.getWidth() - 1) {
            if (y == 0) {
                return '┐';
            } else if (y == size.getHeight() - 1) {
                return '┘';
            } else {
                return '│';
            }
        } else {
            if (y == 0 || y == size.getHeight() - 1) {
                return '─';
            } else if (child != null) {
                return child.draw(x - 1, y - 1);
            } else {
                return null;
            }
        }
    }
}
