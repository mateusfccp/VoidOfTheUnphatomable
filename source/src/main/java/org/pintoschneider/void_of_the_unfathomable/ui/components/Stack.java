package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

/**
 * A component that arranges its child components in a depth stack.
 * <p>
 * Children added later are drawn on top of earlier children.
 */
public class Stack extends Component {
    final Component[] children;

    /**
     * Creates a new Stack component with the given children.
     *
     * @param children The child components to be stacked.
     */
    public Stack(Component... children) {
        this.children = children;
    }

    @Override
    public void layout(Constraints constraints) {
        final Constraints childrenConstraints = constraints.loosen();
        int maxWidth = 0;
        int maxHeight = 0;

        for (Component child : children) {
            child.layout(childrenConstraints);
            maxWidth = Math.max(maxWidth, child.size().width());
            maxHeight = Math.max(maxHeight, child.size().height());
        }

        final Size biggestSize = new Size(maxWidth, maxHeight);
        size = constraints.constrain(biggestSize);
    }

    @Override
    public void draw(Canvas canvas) {
        for (Component child : children) {
            canvas.draw(child, Offset.ZERO);
        }
    }
}
