package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;

public class ConstrainedBox extends Component {
    final Component child;
    final Constraints constraints;

    public ConstrainedBox(Constraints constraints, Component child) {
        this.child = child;
        this.constraints = constraints;

        assert child != null : "ConstrainedBox requires a non-null child.";
        assert constraints != null : "ConstrainedBox requires non-null constraints.";
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
