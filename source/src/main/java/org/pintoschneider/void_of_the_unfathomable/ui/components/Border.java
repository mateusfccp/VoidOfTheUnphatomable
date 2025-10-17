package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

/**
 * A bordered box that can contain a single {@link Component} child.
 */
public final class Border extends Component {
    private final Component child;
    private final CornerStyle cornerStyle;
    private final LineStyle lineStyle;

    /**
     * Constructs an empty {@link Border} with no child, using default styles.
     */
    public Border() {
        this(null, CornerStyle.SQUARE, LineStyle.SINGLE);
    }

    /**
     * Constructs a {@link Border} with the given child, using default styles.
     *
     * @param child the child {@link Component} to be contained within the box
     */
    public Border(Component child) {
        this(child, CornerStyle.SQUARE, LineStyle.SINGLE);
    }

    /**
     * Constructs a {@link Border} with the given child, corner style, and line style.
     *
     * @param child       the child {@link Component} to be contained within the box
     * @param cornerStyle the style for the corners
     * @param lineStyle   the style for the horizontal and vertical lines
     */
    public Border(Component child, CornerStyle cornerStyle, LineStyle lineStyle) {
        this.child = child;
        this.cornerStyle = cornerStyle;
        this.lineStyle = lineStyle;
    }

    @Override
    public void layout(Constraints constraints) {
        final Constraints minimalConstraints = new Constraints(2, null, 2, null);

        if (child == null) {
            size = constraints.enforce(minimalConstraints).smallest();
        } else {
            // We first lay out the child with the parent constraints so it can determine its size
            child.layout(constraints);

            final Constraints bordersConstraints = new Constraints(
                2,
                child.size().width() + 2,
                2,
                child.size().height() + 2
            ).enforce(constraints);
            size = bordersConstraints.biggest();
            final Constraints childConstraints = Constraints.loose(
                Math.max(0, size.width() - 2),
                Math.max(0, size.height() - 2)
            );

            // Then we lay out the child again with the adjusted constraints to fit within the border
            child.layout(childConstraints);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // Draw corners
        canvas.draw(cornerStyle.topLeft, 0, 0);
        canvas.draw(cornerStyle.topRight, size.width() - 1, 0);
        canvas.draw(cornerStyle.bottomLeft, 0, size.height() - 1);
        canvas.draw(cornerStyle.bottomRight, size.width() - 1, size.height() - 1);

        // Draw horizontal lines
        for (int x = 1; x < size.width() - 1; x++) {
            canvas.draw(lineStyle.horizontal, x, 0);
            canvas.draw(lineStyle.horizontal, x, size.height() - 1);
        }

        // Draw vertical lines
        for (int y = 1; y < size.height() - 1; y++) {
            canvas.draw(lineStyle.vertical, 0, y);
            canvas.draw(lineStyle.vertical, size.width() - 1, y);
        }

        if (child != null) {
            canvas.draw(child, 1, 1);
        }
    }

    /**
     * Represents the style of the corners of a border.
     */
    public static class CornerStyle {
        public final char topLeft;
        public final char topRight;
        public final char bottomLeft;
        public final char bottomRight;

        private CornerStyle(char topLeft, char topRight, char bottomLeft, char bottomRight) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }

        /**
         * Square corners (single line).
         * <p>
         * <pre>{@code
         * ┌ ┐
         * └ ┘
         * }</pre>
         */
        public static final CornerStyle SQUARE = new CornerStyle('┌', '┐', '└', '┘');

        /**
         * Double line corners.
         * <p>
         * <pre>{@code
         * ╔ ╗
         * ╚ ╝
         * }</pre>
         */
        public static final CornerStyle DOUBLE = new CornerStyle('╔', '╗', '╚', '╝');

        /**
         * Rounded corners.
         * <p>
         * <pre>{@code
         * ╭ ╮
         * ╰ ╯
         * }</pre>
         */
        public static final CornerStyle ROUNDED = new CornerStyle('╭', '╮', '╰', '╯');

        /**
         * Heavy line corners.
         * <p>
         * <pre>{@code
         * ┏ ┓
         * ┗ ┛
         * }</pre>
         */
        public static final CornerStyle HEAVY = new CornerStyle('┏', '┓', '┗', '┛');
    }

    /**
     * Represents the style of the lines of a border.
     */
    public static class LineStyle {
        public final char horizontal;
        public final char vertical;

        private LineStyle(char horizontal, char vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        /**
         * Single line style.
         * <p>
         * <pre>{@code
         * ─ │
         * }</pre>
         */
        public static final LineStyle SINGLE = new LineStyle('─', '│');

        /**
         * Double line style.
         * <p>
         * <pre>{@code
         * ═ ║
         * }</pre>
         */
        public static final LineStyle DOUBLE = new LineStyle('═', '║');

        /**
         * Dashed line style.
         * <p>
         * <pre>{@code
         * ┄ ┆
         * }</pre>
         */
        public static final LineStyle DASHED = new LineStyle('┄', '┆');

        /**
         * Dotted line style.
         * <p>
         * <pre>{@code
         * ┈ ┊
         * }</pre>
         */
        public static final LineStyle DOTTED = new LineStyle('┈', '┊');

        /**
         * Large dashed line style.
         * <p>
         * <pre>{@code
         * ╌ ╎
         * }</pre>
         */
        public static final LineStyle DASHED_LARGE = new LineStyle('╌', '╎');

        /**
         * Heavy line style.
         * <p>
         * <pre>{@code
         * ━ ┃
         * }</pre>
         */
        public static final LineStyle HEAVY = new LineStyle('━', '┃');
    }
}
