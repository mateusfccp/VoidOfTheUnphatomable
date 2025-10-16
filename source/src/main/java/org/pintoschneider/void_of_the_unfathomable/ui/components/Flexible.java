package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.DataComponent;

import java.util.Objects;

public class Flexible extends DataComponent {
    public Flexible(int flex, Component child) {
        assert flex > 0 : "Flexible requires a positive flex value.";

        super(
            new FlexibleData(flex),
            Objects.requireNonNull(child, "Flexible requires a non-null child.")
        );
    }
}
