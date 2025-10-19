package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

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

    private MainAxisSize mainAxisSize = MainAxisSize.MAX;

    private CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.START;

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

    /**
     * Sets how much space the layout should occupy in the main axis.
     *
     * @param mainAxisSize the main axis size to set
     */
    public LinearLayout mainAxisSize(MainAxisSize mainAxisSize) {
        this.mainAxisSize = Objects.requireNonNullElse(mainAxisSize, MainAxisSize.MAX);
        return this;
    }

    /**
     * Sets the cross-axis alignment for this layout.
     *
     * @param crossAxisAlignment the cross-axis alignment to set
     */
    public LinearLayout crossAxisAlignment(CrossAxisAlignment crossAxisAlignment) {
        this.crossAxisAlignment = Objects.requireNonNullElse(crossAxisAlignment, CrossAxisAlignment.START);
        return this;
    }

    @Override
    public void layout(Constraints constraints) {
        if (children.length == 0) {
            size = constraints.smallest();
            return;
        }

        size = constraints.biggest();
        int availableSpace = mainAxisLength();
        int usedSpace = 0;
        int totalFlex = 0;

        for (Component child : children) {
            final Object data = child.data();

            if (data instanceof FlexibleData(int flex)) {
                totalFlex += flex;
            } else {
                final Constraints childConstraints = switch (orientation) {
                    case HORIZONTAL -> new Constraints(
                        0,
                        availableSpace,
                        crossAxisAlignment == CrossAxisAlignment.STRETCH ? crossAxisLength() : 0,
                        crossAxisLength()
                    );
                    case VERTICAL -> new Constraints(
                        crossAxisAlignment == CrossAxisAlignment.STRETCH ? crossAxisLength() : 0,
                        crossAxisLength(),
                        0,
                        availableSpace
                    );
                };

                child.layout(childConstraints);

                availableSpace = availableSpace - getChildMainAxisLength(child);
                usedSpace = usedSpace + getChildMainAxisLength(child);
            }
        }

        for (Component child : children) {
            final Object data = child.data();

            if (data instanceof FlexibleData(int flex)) {
                final int length = (int) ((flex / (float) totalFlex) * availableSpace);

                final Constraints childConstraints = switch (orientation) {
                    case HORIZONTAL -> new Constraints(
                        length,
                        length,
                        crossAxisAlignment == CrossAxisAlignment.STRETCH ? crossAxisLength() : 0,
                        crossAxisLength()
                    );
                    case VERTICAL -> new Constraints(
                        crossAxisAlignment == CrossAxisAlignment.STRETCH ? crossAxisLength() : 0,
                        crossAxisLength(),
                        length,
                        length
                    );
                };

                child.layout(childConstraints);

                usedSpace = usedSpace + getChildMainAxisLength(child);
            }
        }

        switch (mainAxisSize) {
            case MIN -> size = switch (orientation) {
                case HORIZONTAL -> new Size(usedSpace, constraints.maxHeight());
                case VERTICAL -> new Size(constraints.maxWidth(), usedSpace);
            };
            case MAX -> size = constraints.biggest();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (children.length == 0) return;

        int mainAxisPosition = 0;

        for (Component child : children) {
            final int crossAxisPosition = switch (crossAxisAlignment) {
                case START, STRETCH -> 0;
                case CENTER ->
                    (crossAxisLength() - getChildCrossAxisLength(child)) / 2 - getChildCrossAxisLength(child) / 2;
                case END -> crossAxisLength() - getChildCrossAxisLength(child);
            };

            switch (orientation) {
                case HORIZONTAL -> {
                    canvas.draw(child, mainAxisPosition, crossAxisPosition);
                    mainAxisPosition += child.size().width();
                }
                case VERTICAL -> {
                    canvas.draw(child, crossAxisPosition, mainAxisPosition);
                    mainAxisPosition += child.size().height();
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

    private int getChildCrossAxisLength(Component component) {
        return switch (orientation) {
            case HORIZONTAL -> component.size().height();
            case VERTICAL -> component.size().width();
        };
    }
}

