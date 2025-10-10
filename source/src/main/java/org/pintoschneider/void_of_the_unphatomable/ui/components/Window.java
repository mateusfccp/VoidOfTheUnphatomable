package org.pintoschneider.void_of_the_unphatomable.ui.components;

import org.pintoschneider.void_of_the_unphatomable.ui.core.Drawable;
import org.pintoschneider.void_of_the_unphatomable.ui.Size;

public final class Window extends Drawable {
    private final Drawable child;

    public Window() {
        this.child = null;
    }

    public Window(Drawable child) {
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
