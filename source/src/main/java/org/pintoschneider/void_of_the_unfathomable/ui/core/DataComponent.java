package org.pintoschneider.void_of_the_unfathomable.ui.core;

import java.util.Objects;

public abstract class DataComponent extends Component {
    private final Component child;

    public DataComponent(Object data, Component child) {
        this.data = data;
        this.child = Objects.requireNonNull(child);
    }

    @Override
    public void layout(Constraints constraints) {
        child.layout(constraints);
        size = child.size();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(child, 0, 0);
    }
}
