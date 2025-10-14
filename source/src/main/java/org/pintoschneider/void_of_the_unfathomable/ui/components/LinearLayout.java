package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

/**
 * A general-purpose layout component that arranges its children linearly,
 * either horizontally (like a Row) or vertically (like a Column).
 * <p>
 * Use {@link Row} or {@link Column} for convenience.
 */
public class LinearLayout extends Component {
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
        public final Component child;

        public Item(Component child) {
            this.child = child;
        }
    }

    /**
     * A child that uses its intrinsic size in the layout direction.
     */
    public static final class Intrinsic extends Item {
        public Intrinsic(Component child) {
            super(child);
        }
    }

    /**
     * A flexible-size child.
     */
    public static final class Flexible extends Item {
        private final int flex;

        public Flexible(int flex, Component child) {
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

        int availableSpace = mainAxisLength();
        int totalFlex = 0;

        for (Item item : items) {
            switch (item) {
                case Intrinsic _ -> {
                    final Constraints childConstraints = switch (orientation) {
                        case HORIZONTAL -> Constraints.loose(availableSpace, crossAxisLength());
                        case VERTICAL -> Constraints.loose(crossAxisLength(), availableSpace);
                    };

                    item.child.layout(childConstraints);

                    availableSpace = availableSpace - getChildMainAxisLength(item.child);
                }
                case Flexible flexible -> totalFlex += flexible.flex;
            }
        }

        for (Item item : items) {
            if (item instanceof Flexible flexible) {
                final int length = (int) ((flexible.flex / (float) totalFlex) * availableSpace);

                final Constraints childConstraints = switch (orientation) {
                    case HORIZONTAL -> Constraints.tight(length, crossAxisLength());
                    case VERTICAL -> Constraints.tight(crossAxisLength(), length);
                };

                item.child.layout(childConstraints);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (items == null || items.length == 0) return;

        int position = 0;

        for (Item item : items) {
            switch (orientation) {
                case HORIZONTAL -> {
                    canvas.draw(item.child, position, 0);
                    position += item.child.size().width();
                }
                case VERTICAL -> {
                    canvas.draw(item.child, 0, position);
                    position += item.child.size().height();
                }
            }
        }
    }

    private int mainAxisLength() {
        return switch (orientation) {
            case HORIZONTAL -> size.width();
            case VERTICAL -> size.height();
        };
    }

    private int crossAxisLength() {
        return switch (orientation) {
            case HORIZONTAL -> size.height();
            case VERTICAL -> size.width();
        };
    }

    private int getChildMainAxisLength(Component component) {
        return switch (orientation) {
            case HORIZONTAL -> component.size().width();
            case VERTICAL -> component.size().height();
        };
    }
}
