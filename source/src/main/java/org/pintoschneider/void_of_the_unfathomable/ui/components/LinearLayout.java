package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

import java.util.Objects;

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
     * The child items to be arranged.
     */
    protected final Component[] children;

    /**
     * The orientation of this layout.
     */
    protected final Orientation orientation;

    /**
     * Constructs a LinearLayout with the specified orientation and items.
     *
     * @param orientation the orientation (horizontal or vertical)
     * @param children    the children as Fixed or Flexible items
     */
    public LinearLayout(Orientation orientation, Component... children) {
        this.orientation = Objects.requireNonNull(orientation);
        this.children = Objects.requireNonNull(children);
    }

    @Override
    public void layout(Constraints constraints) {
        size = constraints.biggest();
        if (children.length == 0) return;

        int availableSpace = mainAxisLength();
        int totalFlex = 0;

        for (Component child : children) {
            final Object data = child.data();

            if (data instanceof FlexibleData(int flex)) {
                totalFlex += flex;
            } else {
                final Constraints childConstraints = switch (orientation) {
                    case HORIZONTAL -> Constraints.loose(availableSpace, crossAxisLength());
                    case VERTICAL -> Constraints.loose(crossAxisLength(), availableSpace);
                };

                child.layout(childConstraints);

                availableSpace = availableSpace - getChildMainAxisLength(child);
            }
        }

        for (Component child : children) {
            final Object data = child.data();

            if (data instanceof FlexibleData(int flex)) {
                final int length = (int) ((flex / (float) totalFlex) * availableSpace);

                final Constraints childConstraints = switch (orientation) {
                    case HORIZONTAL -> Constraints.tight(length, crossAxisLength());
                    case VERTICAL -> Constraints.tight(crossAxisLength(), length);
                };

                child.layout(childConstraints);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (children.length == 0) return;

        int position = 0;

        for (Component child : children) {
            switch (orientation) {
                case HORIZONTAL -> {
                    canvas.draw(child, position, 0);
                    position += child.size().width();
                }
                case VERTICAL -> {
                    canvas.draw(child, 0, position);
                    position += child.size().height();
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

