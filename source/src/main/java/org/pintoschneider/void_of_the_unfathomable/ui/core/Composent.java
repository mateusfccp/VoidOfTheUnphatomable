package org.pintoschneider.void_of_the_unfathomable.ui.core;

/**
 * A component that is the composition of other components.
 */
public abstract class Composent extends Component {
    private Component child;

    protected Composent() {
    }

    @Override
    public void layout(Constraints constraints) {
        child = build();
        child.layout(constraints);

        size = child.size();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.draw(child, 0, 0);
    }

    /**
     * Builds this component.
     *
     * @return The built component.
     */
    public abstract Component build();
}
