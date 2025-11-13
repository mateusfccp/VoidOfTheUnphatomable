package org.pintoschneider.void_of_the_unfathomable.ui.core;

import java.util.Objects;

/**
 * A component that holds data and has a single child component.
 */
public abstract class DataComponent extends Component {
    private final Component child;

    /**
     * The data associated with this component.
     *
     * @param data  The data associated with this component.
     * @param child The child component.
     */
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
