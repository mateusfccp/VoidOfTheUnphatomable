package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;

/**
 * Represents the style of the border a border.
 * <p>
 * Can be used with {@link Box} to draw borders around components.
 *
 * @param horizontal  The character used for the horizontal lines.
 * @param vertical    The character used for the vertical lines.
 * @param topLeft     The character used for the top left corner.
 * @param topRight    The character used for the top right corner.
 * @param bottomLeft  The character used for the bottom left corner.
 * @param bottomRight The character used for the bottom right corner.
 */
public record Border(char horizontal, char vertical, char topLeft, char topRight, char bottomLeft, char bottomRight) {
    /**
     * A single line border.
     */
    public static final Border SINGLE = new Border('─', '│', '┌', '┐', '└', '┘');

    /**
     * A double line border.
     */
    public static final Border DOUBLE = new Border('═', '║', '╔', '╗', '╚', '╝');

}