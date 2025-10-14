package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.core.*;

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
        final char[] frames = new char[]{'⠋', '⠙', '⠹', '⠸', '⠼', '⠤', '⠦', '⠧', '⠇', '⠏' };
        canvas.draw(String.valueOf(frames[frame % frames.length]).charAt(0), 0, 0);
    }
}
