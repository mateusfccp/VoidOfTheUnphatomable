package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

/**
 * A bordered box that can contain a single {@link Component} child.
 */
public final class Border extends Component {
    private final Component child;

    /**
     * Constructs an empty {@link Border} with no child.
     */
    public Border() {
        this.child = null;
    }

    /**
     * Constructs a {@link Border} with the given child.
     *
     * @param child the child {@link Component} to be contained within the box
     */
    public Border(Component child) {
        this.child = child;
    }

    @Override
    public void layout(Constraints constraints) {
        final Constraints minimalConstraints = new Constraints(2, Integer.MAX_VALUE, 2, Integer.MAX_VALUE);

        if (child == null) {
            size = constraints.enforce(minimalConstraints).smallest();
        } else {
            // We first lay out the child with the parent constraints so it can determine its size
            child.layout(constraints);

            final Constraints bordersConstraints = Constraints.tight(child.size()).enforce(minimalConstraints);
            size = bordersConstraints.biggest();
            final Constraints childConstraints = Constraints.loose(
                    Math.max(0, size.width() - 2),
                    Math.max(0, size.height() - 2)
            );

            // Then we lay out the child again with the adjusted constraints to fit within the border
            child.layout(childConstraints);
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

//        for (int y = 1; y < size.height() - 1; y++) {
//            for (int x = 1; x < size.width() - 1; x++) {
//                canvas.draw('░', x, y);
//            }
//        }

        if (child != null) {
            canvas.draw(child, 1, 1);
        }
    }
}
