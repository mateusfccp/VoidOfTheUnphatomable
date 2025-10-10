package org.pintoschneider.void_of_the_unfathomable.ui.components;

/**
 * A horizontal linear layout that arranges its children in a row.
 *
 * @see LinearLayout
 */
public class Row extends LinearLayout {
    /**
     * Constructs a Row with the given children as Fixed or Flexible items.
     *
     * @param items the children as LinearLayout.Item
     */
    public Row(Item... items) {
        super(Orientation.HORIZONTAL, items);
    }
}
