package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

/**
 * A vertical linear layout that arranges its children in a column.
 *
 * @see LinearLayout
 */
public class Column extends LinearLayout {
    /**
     * Constructs a Column with the given children as Fixed or Flexible items.
     *
     * @param items the children as LinearLayout.Item
     */
    public Column(Component... items) {
        super(Orientation.VERTICAL, items);
    }
}
