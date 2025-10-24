package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;

/**
 * A component that displays a spinning character based on the current frame.
 * <p>
 * This component is typically used to indicate an idle state or a loading where we don't know the duration.
 */
public class IdleSpinner extends Component {
    final int frame;

    public IdleSpinner(int frame) {
        this.frame = frame;
    }

    @Override
    public void layout(org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints constraints) {
        size = new Size(1, 1);
    }

    @Override
    public void draw(Canvas canvas) {
        final char[] frames = new char[]{'⠇', '⠋', '⠙', '⠸', '⠴', '⠦'};
        canvas.draw(String.valueOf(frames[frame % frames.length]).charAt(0), 0, 0);
    }
}
