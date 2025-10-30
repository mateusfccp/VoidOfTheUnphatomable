package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.DataComponent;

import java.util.Objects;

/**
 * A component that expands to fill available space according to a flex factor.
 * <p>
 * Flexible will only take effect inside components that may use its flex value, like {@link Row} or {@link Column}.
 */
public class Flexible extends DataComponent {
    /**
     * Creates a new Flexible component with a default flex factor of 1.
     *
     * @param child The child component to be made flexible.
     */
    public Flexible(Component child) {
        this(1, child);
    }

    /**
     * Creates a new Flexible component.
     *
     * @param flex  The flex factor determining how much space this component should take relative to its siblings.
     * @param child The child component to be made flexible.
     */
    public Flexible(int flex, Component child) {
        super(
            new FlexibleData(flex),
            Objects.requireNonNull(child, "Flexible requires a non-null child.")
        );

        assert flex > 0 : "Flexible requires a positive flex value.";
    }
}
