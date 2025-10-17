package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

/**
 * A horizontal linear layout that arranges its children in a row.
 *
 * @see LinearLayout
 */
public class Row extends LinearLayout {
    /**
     * Constructs a Row with the given children as Fixed or Flexible items.
     *
     * @param children The children of the row.
     */
    public Row(Component... children) {
        super(Orientation.HORIZONTAL, children);
    }
}
