package org.pintoschneider.void_of_the_unphatomable.ui.components;

import org.pintoschneider.void_of_the_unphatomable.ui.core.Drawable;
import org.pintoschneider.void_of_the_unphatomable.ui.Size;

/**
 * A general-purpose layout component that arranges its children linearly,
 * either horizontally (like a Row) or vertically (like a Column).
 * <p>
 * Use {@link Row} or {@link Column} for convenience.
 */
public class LinearLayout extends Drawable {
    /**
     * The orientation of the layout: horizontal (row) or vertical (column).
     */
    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    /**
     * Represents a child of {@link LinearLayout}, either with a fixed size or a flexible size.
     */
    public static sealed abstract class Item permits Fixed, Flexible {
        public final Drawable child;

        public Item(Drawable child) {
            this.child = child;
        }
    }

    /**
     * A fixed-size child.
     */
    public static final class Fixed extends Item {
        private final int size;

        public Fixed(Drawable child, int size) {
            super(child);
            this.size = size;
        }
    }

    /**
     * A flexible-size child.
     */
    public static final class Flexible extends Item {
        private final int flex;

        public Flexible(Drawable child, int flex) {
            super(child);
            this.flex = flex;
        }
    }

    /**
     * The child items to be arranged.
     */
    protected final Item[] items;

    /**
     * The orientation of this layout.
     */
    protected final Orientation orientation;

    /**
     * Constructs a LinearLayout with the specified orientation and items.
     *
     * @param orientation the orientation (horizontal or vertical)
     * @param items       the children as Fixed or Flexible items
     */
    public LinearLayout(Orientation orientation, Item... items) {
        this.orientation = orientation;
        this.items = items;
    }

    /**
     * Lays out the children within the given maximum size, dividing space
     * according to fixed sizes and flex proportions.
     *
     * @param maximumSize the maximum size available for layout
     */
    @Override
    public void layout(Size maximumSize) {
        size = maximumSize;
        if (items == null || items.length == 0) return;


        int totalFixed = 0;
        int totalFlex = 0;
        for (Item item : items) {
            if (item instanceof Fixed fixed) {
                totalFixed += fixed.size;
            } else if (item instanceof Flexible flexible) {
                totalFlex += flexible.flex;
            }
        }

        final int available = switch (orientation) {
            case HORIZONTAL -> size.getWidth() - totalFixed;
            case VERTICAL -> size.getHeight() - totalFixed;
        };

        for (Item item : items) {
            final int length = switch (item) {
                case Fixed fixed -> fixed.size;
                case Flexible flexible -> available * flexible.flex / totalFlex;
            };

            final Size size = switch (orientation) {
                case HORIZONTAL -> new Size(length, this.size.getHeight());
                case VERTICAL -> new Size(this.size.getWidth(), length);
            };

            item.child.layout(size);
        }
    }

    /**
     * Draws the character at the given coordinates by delegating to the appropriate child.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the character to draw, or null if none
     */
    @Override
    public Character draw(int x, int y) {
        if (items == null || items.length == 0) return null;

        int totalFixed = 0;
        int totalFlex = 0;
        for (Item item : items) {
            switch (item) {
                case Fixed fixed -> totalFixed += fixed.size;
                case Flexible flexible -> totalFlex += flexible.flex;
            }
        }

        final int available = switch (orientation) {
            case HORIZONTAL -> size.getWidth() - totalFixed;
            case VERTICAL -> size.getHeight() - totalFixed;
        };

        int position = 0;
        for (Item item : items) {
            final int length = switch (item) {
                case Fixed fixed -> fixed.size;
                case Flexible flexible -> available * flexible.flex / totalFlex;
            };

            boolean inside = switch (orientation) {
                case HORIZONTAL -> x < position + length;
                case VERTICAL -> y < position + length;
            };

            if (inside) {
                return switch (orientation) {
                    case HORIZONTAL -> item.child.draw(x - position, y);
                    case VERTICAL -> item.child.draw(x, y - position);
                };
            }
            position += length;
        }

        return null;
    }
}
