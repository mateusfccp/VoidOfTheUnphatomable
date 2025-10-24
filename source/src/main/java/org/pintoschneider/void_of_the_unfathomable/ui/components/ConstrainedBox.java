package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

import java.util.Objects;

/**
 * A component that imposes additional constraints on its child component.
 */
public class ConstrainedBox extends Component {
    private final Component child;
    private final Constraints constraints;

    /**
     * Constructs a ConstrainedBox with the given constraints and child component.
     *
     * @param constraints the constraints to impose on the child
     * @param child       the child component to be constrained
     */
    public ConstrainedBox(Constraints constraints, Component child) {
        this.child = Objects.requireNonNull(child);
        this.constraints = Objects.requireNonNull(constraints);
    }

    @Override
    public void layout(Constraints constraints) {
        child.layout(this.constraints.enforce(constraints));
        size = child.size();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(child, 0, 0);
    }
}
