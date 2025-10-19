package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

/**
 * A vertical divider component that draws a vertical line.
 */
public final class VerticalDivider extends Component {
    @Override
    public void layout(Constraints constraints) {
        size = new Size(1, constraints.maxHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        for (int y = 0; y < size.height(); y++) {
            canvas.draw('│', 0, y);
        }
    }
}
