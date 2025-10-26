package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.pintoschneider.void_of_the_unfathomable.ui.components.Box;

/**
 * Represents the style of the border a border.
 * <p>
 * Can be used with {@link Box} to draw borders around components.
 */
public record Border(char horizontal, char vertical, char topLeft, char topRight, char bottomLeft, char bottomRight) {
    public static final Border SINGLE = new Border('─', '│', '┌', '┐', '└', '┘');
    public static final Border SINGLE_ROUNDED = new Border('─', '│', '╭', '╮', '╰', '╯');
    public static final Border DOUBLE = new Border('═', '║', '╔', '╗', '╚', '╝');
    public static final Border DASHED = new Border('┄', '┆', '┌', '┐', '└', '┘');
    public static final Border DOTTED = new Border('┈', '┊', '┌', '┐', '└', '┘');
    public static final Border DASHED_LARGE = new Border('╌', '╎', '┌', '┐', '└', '┘');
    public static final Border HEAVY = new Border('━', '┃', '┏', '┓', '┗', '┛');
}