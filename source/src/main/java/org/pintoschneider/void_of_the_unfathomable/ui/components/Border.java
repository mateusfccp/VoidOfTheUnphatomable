package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Drawable;

/**
 * A bordered box that can contain a single {@link Drawable} child.
 */
public final class Border extends Drawable {
    private final Drawable child;

    /**
     * Constructs an empty {@link Border} with no child.
     */
    public Border() {
        this.child = null;
    }

    /**
     * Constructs a {@link Border} with the given child.
     *
     * @param child the child {@link Drawable} to be contained within the box
     */
    public Border(Drawable child) {
        this.child = child;
    }

    @Override
    public void layout(Constraints constraints) {
        size = constraints.biggest();

        if (child != null) {
            final Constraints innerConstraints = new Constraints(0, size.width() - 2, 0, size.height() - 2);
            child.layout(innerConstraints);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw('┌', 0, 0);
        canvas.draw('┐', size.width() - 1, 0);
        canvas.draw('└', 0, size.height() - 1);
        canvas.draw('┘', size.width() - 1, size.height() - 1);

        for (int x = 1; x < size.width() - 1; x++) {
            canvas.draw('─', x, 0);
            canvas.draw('─', x, size.height() - 1);
        }

        for (int y = 1; y < size.height() - 1; y++) {
            canvas.draw('│', 0, y);
            canvas.draw('│', size.width() - 1, y);
        }

        if (child != null) {
            canvas.draw(child, 1, 1);
        }
    }
}
