package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Drawable;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Size;

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
     * Represents a child of {@link LinearLayout}, either with intrinsic size or a flexible size.
     */
    public static sealed abstract class Item permits Intrinsic, Flexible {
        public final Drawable child;

        public Item(Drawable child) {
            this.child = child;
        }
    }

    /**
     * A child that uses its intrinsic size in the layout direction.
     */
    public static final class Intrinsic extends Item {
        public Intrinsic(Drawable child) {
            super(child);
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

    @Override
    public void layout(Constraints constraints) {
        size = constraints.biggest();
        if (items == null || items.length == 0) return;

        int totalIntrinsic = 0;
        int totalFlex = 0;
        for (Item item : items) {
            if (item instanceof Intrinsic intrinsic) {
                intrinsic.child.layout(constraints);
                totalIntrinsic += switch (orientation) {
                    case HORIZONTAL -> intrinsic.child.size().width();
                    case VERTICAL -> intrinsic.child.size().height();
                };
            } else if (item instanceof Flexible flexible) {
                totalFlex += flexible.flex;
            }
        }

        final int available = switch (orientation) {
            case HORIZONTAL -> size.width() - totalIntrinsic;
            case VERTICAL -> size.height() - totalIntrinsic;
        };

        for (Item item : items) {
            final int length = switch (item) {
                case Intrinsic intrinsic -> switch (orientation) {
                    case HORIZONTAL -> intrinsic.child.size().width();
                    case VERTICAL -> intrinsic.child.size().height();
                };
                case Flexible flexible -> available * flexible.flex / totalFlex;
            };

            final Size size = switch (orientation) {
                case HORIZONTAL -> new Size(length, this.size.height());
                case VERTICAL -> new Size(this.size.width(), length);
            };

            final Constraints itemConstraints = Constraints.tight(size.width(), size.height());

            item.child.layout(itemConstraints);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (items == null || items.length == 0) return;

        int totalIntrinsic = 0;
        int totalFlex = 0;
        for (Item item : items) {
            switch (item) {
                case Intrinsic intrinsic -> {
                    totalIntrinsic += switch (orientation) {
                        case HORIZONTAL -> intrinsic.child.size().width();
                        case VERTICAL -> intrinsic.child.size().height();
                    };
                }
                case Flexible flexible -> totalFlex += flexible.flex;
            }
        }

        final int available = switch (orientation) {
            case HORIZONTAL -> size.width() - totalIntrinsic;
            case VERTICAL -> size.height() - totalIntrinsic;
        };

        int position = 0;
        for (Item item : items) {
            final int length = switch (item) {
                case Intrinsic intrinsic -> switch (orientation) {
                    case HORIZONTAL -> intrinsic.child.size().width();
                    case VERTICAL -> intrinsic.child.size().height();
                };
                case Flexible flexible -> available * flexible.flex / totalFlex;
            };

            switch (orientation) {
                case HORIZONTAL -> canvas.draw(item.child, position, 0);
                case VERTICAL -> canvas.draw(item.child, 0, position);
            }

            position += length;
        }
    }
}
