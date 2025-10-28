package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

/**
 * A component that has a fixed size.
 * <p>
 * The child is laid out with loose constraints, meaning it can be smaller than the box size.
 */
public final class SizedBox extends Component {
    private final Size boxSize;
    private final Component child;

    /**
     * Creates a SizedBox with the given size and child.
     *
     * @param boxSize The fixed size of the box.
     * @param child   The child component to be contained within the box.
     */
    public SizedBox(Size boxSize, Component child) {
        this.boxSize = boxSize;
        this.child = child;
    }

    /**
     * Creates a SizedBox with the given width, height and no child.
     *
     * @param width  The fixed width of the box.
     * @param height The fixed height of the box.
     */
    public SizedBox(int width, int height) {
        this(new Size(width, height), null);
    }

    /**
     * Creates a SizedBox with the given width, height and child.
     *
     * @param width  The fixed width of the box.
     * @param height The fixed height of the box.
     * @param child  The child component to be contained within the box.
     */
    public SizedBox(int width, int height, Component child) {
        this(new Size(width, height), child);
    }


    @Override
    public void layout(Constraints constraints) {
        size = constraints.enforce(Constraints.tight(boxSize)).biggest();

        if (child != null) {
            final Constraints childConstraints = Constraints.loose(boxSize.width(), boxSize.height());
            child.layout(childConstraints);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (child != null) {
            canvas.draw(child, 0, 0);
        }
    }
}
