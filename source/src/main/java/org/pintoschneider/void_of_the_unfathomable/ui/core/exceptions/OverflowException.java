package org.pintoschneider.void_of_the_unfathomable.ui.core.exceptions;

import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

import java.util.Objects;

/**
 * Exception thrown when a component tries to render outside its allocated size.
 * <p>
 * This typically indicates a layout issue where the component's size constraints were not properly enforced or
 * respected, or the available space was insufficient for the component's content.
 */
public class OverflowException extends UIException {
    private final Component component;
    private final Offset offset;

    /**
     * Constructs a new OverflowException for the given component and offset.
     *
     * @param component The component that overflowed.
     * @param offset    The offset at which the overflow occurred.
     */
    public OverflowException(Component component, Offset offset) {
        this.component = Objects.requireNonNull(component);
        this.offset = Objects.requireNonNull(offset);
        super(
            "Component %s overflowed at offset %s. Expected to be within %s".formatted(
                component,
                offset,
                component.size()
            )
        );
    }

    /**
     * Returns the component that caused the overflow.
     *
     * @return The overflowing component.
     */
    public Component component() {
        return component;
    }

    /**
     * Returns the offset at which the overflow occurred.
     *
     * @return The overflow offset.
     */
    public Offset offset() {
        return offset;
    }
}
