package org.pintoschneider.void_of_the_unfathomable.game.scenes;

import org.pintoschneider.void_of_the_unfathomable.engine.Key;
import org.pintoschneider.void_of_the_unfathomable.engine.Scene;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Column;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Padding;
import org.pintoschneider.void_of_the_unfathomable.ui.components.Text;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

import java.util.List;
import java.util.stream.IntStream;

/**
 * A scene that renders an interface for selecting options.
 */
public abstract class SelectionScene implements Scene {
    private int currentIndex = 0;

    /**
     * Gets the list of options available for selection.
     *
     * @return A list of options.
     */
    abstract List<Option> options();

    /**
     * Gets the alignment for the options.
     *
     * @return The cross axis alignment.
     */
    protected CrossAxisAlignment alignment() {
        return CrossAxisAlignment.START;
    }

    @Override
    public Component build() {
        return new Box(
            Border.SINGLE_ROUNDED,
            new Padding(
                EdgeInsets.all(1),
                buildContent()
            )
        );
    }

    private Component buildContent() {
        final List<Option> options = options();

        final Component[] items = IntStream
            .range(0, options.size())
            .mapToObj(i -> options.get(i).toComponent(i == currentIndex))
            .toArray(Component[]::new);

        return new Column(items)
            .crossAxisAlignment(alignment())
            .mainAxisSize(MainAxisSize.MIN);
    }

    @Override
    public void onKeyPress(Key key) {
        if (!options().isEmpty()) {
            if (key == Key.UP) {
                moveSelectionUp();
            } else if (key == Key.DOWN) {
                moveSelectionDown();
            } else if (key == Key.ENTER) {
                final Option option = options().get(currentIndex);

                if (option.enabled()) {
                    options().get(currentIndex).onSelect().run();
                }
            }
        }
    }

    private void moveSelectionUp() {
        currentIndex = (currentIndex - 1 + options().size()) % options().size();
    }

    private void moveSelectionDown() {
        currentIndex = (currentIndex + 1) % options().size();
    }

    /**
     * Gets the current selected index.
     *
     * @return The current selected index.
     */
    protected int currentIndex() {
        return currentIndex;
    }

    /**
     * Represents an option in the selection scene.
     *
     * @param label    The label of the option.
     * @param onSelect The action to perform when the option is selected.
     * @param enabled  Whether the option is enabled or disabled.
     */
    public record Option(String label, Runnable onSelect, boolean enabled) {
        /**
         * Creates a disabled option.
         *
         * @param label The label of the option.
         */
        public Option(String label) {
            this(label, () -> {}, false);
        }

        /**
         * Creates an enabled option.
         *
         * @param label    The label of the option.
         * @param onSelect The action to perform when the option is selected.
         */
        public Option(String label, Runnable onSelect) {
            this(label, onSelect, true);
        }

        private Component toComponent(boolean isCurrent) {
            final Paint paint;

            if (isCurrent) {
                paint = Paint.INVERTED;
            } else if (!enabled) {
                paint = Paint.DIM;
            } else {
                paint = null;
            }

            return new Text(label, paint);
        }
    }
}
