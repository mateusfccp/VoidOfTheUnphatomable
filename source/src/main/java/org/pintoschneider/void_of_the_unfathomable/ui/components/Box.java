package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

/**
 * A box that can contain a single {@link Component} child.
 */
public final class Box extends Component {
    private final Component child;
    private final Border border;

    /**
     * Constructs an empty {@link Box} with no child, using default styles.
     */
    public Box() {
        this(null, null);
    }

    /**
     * Constructs a {@link Box} with the given child, using default styles.
     *
     * @param child the child {@link Component} to be contained within the box
     */
    public Box(Component child) {
        this(null, child);
    }

    /**
     * Constructs a {@link Box} with the given child and border.
     *
     * @param child  The child {@link Component} to be contained within the box
     * @param border The border definition to use for the box.
     */
    public Box(Border border, Component child) {
        this.child = child;
        this.border = border;
    }

    @Override
    public void layout(Constraints constraints) {
        final int minimumSize = border == null ? 0 : 2;
        final Constraints minimalConstraints = new Constraints(minimumSize, null, minimumSize, null);

        if (child == null) {
            size = constraints.enforce(minimalConstraints).smallest();
        } else {
            // We first lay out the child with the padded constraints so it can determine its size
            final Constraints paddedConstraints = constraints.deflate(EdgeInsets.all(1));
            child.layout(paddedConstraints);

            final Constraints bordersConstraints = new Constraints(
                minimumSize,
                child.size().width() + minimumSize,
                minimumSize,
                child.size().height() + minimumSize
            ).enforce(constraints);
            size = bordersConstraints.biggest();
            final Constraints childConstraints = Constraints.loose(
                Math.max(0, size.width() - minimumSize),
                Math.max(0, size.height() - minimumSize)
            );

            // Then we lay out the child again with the adjusted constraints to fit within the border
            child.layout(childConstraints);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        final int borderOffset = border == null ? 0 : 1;

        // Draw the border if it exists
        if (border != null) {
            // Draw corners (now from lineStyle)
            canvas.draw(border.topLeft(), 0, 0);
            canvas.draw(border.topRight(), size.width() - 1, 0);
            canvas.draw(border.bottomLeft(), 0, size.height() - 1);
            canvas.draw(border.bottomRight(), size.width() - 1, size.height() - 1);

            // Draw horizontal lines
            for (int x = 1; x < size.width() - 1; x++) {
                canvas.draw(border.horizontal(), x, 0);
                canvas.draw(border.horizontal(), x, size.height() - 1);
            }

            // Draw vertical lines
            for (int y = 1; y < size.height() - 1; y++) {
                canvas.draw(border.vertical(), 0, y);
                canvas.draw(border.vertical(), size.width() - 1, y);
            }
        }

        // Fill the inside with spaces
        for (int y = borderOffset; y < size.height() - borderOffset; y++) {
            for (int x = borderOffset; x < size.width() - borderOffset; x++) {
                canvas.draw(' ', x, y);
            }
        }

        // Draw the child component inside the box
        if (child != null) {
            canvas.draw(child, borderOffset, borderOffset);
        }
    }
}
