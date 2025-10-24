package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Alignment;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

import java.util.Objects;

/**
 * A component that aligns its child within itself according to the specified alignment.
 */
public final class Align extends Component {

    final Alignment alignment;
    final Component child;

    /**
     * Constructs an Align component with the specified alignment and child.
     *
     * @param alignment The alignment to use for positioning the child.
     * @param child     The child component to be aligned.
     */
    public Align(Alignment alignment, Component child) {
        this.alignment = alignment;
        this.child = Objects.requireNonNull(child);
    }

    @Override
    public void layout(Constraints constraints) {
        size = constraints.biggest();
        child.layout(Constraints.loose(size));
    }

    @Override
    public void draw(Canvas canvas) {
        final Offset selfCenter = alignment.alongSize(size());
        final Offset childCenter = alignment.alongSize(child.size());
        canvas.draw(child, selfCenter.subtract(childCenter));
    }
}
