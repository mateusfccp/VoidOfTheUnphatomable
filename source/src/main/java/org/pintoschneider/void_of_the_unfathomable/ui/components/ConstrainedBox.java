package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

import java.util.Objects;

public class ConstrainedBox extends Component {
    final Component child;
    final Constraints constraints;

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
