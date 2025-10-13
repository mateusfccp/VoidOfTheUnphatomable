package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.PrintWriter;

/**
 * The {@code Canvas} class represents a 2D grid of characters and optional style objects,
 * used for rendering {@link Drawable} objects in a terminal or text-based UI.
 * <p>
 * Each cell in the canvas can hold a character and an associated style object.
 * The canvas provides methods to draw characters, draw other {@link Drawable} objects,
 * and merge other canvases into itself at specified offsets.
 * <p>
 * The canvas can be written to a {@link PrintWriter} for display.
 */
public final class Canvas {
    Character[][] tiles;
    Paint[][] paints;

    /**
     * Constructs a new {@link Canvas} with the specified width and height.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    public Canvas(int width, int height) {
        tiles = new Character[width][height];
        paints = new Paint[width][height];
    }

    /**
     * Constructs a new {@link Canvas} with the specified size.
     *
     * @param size The size of the canvas.
     */
    public Canvas(Size size) {
        this(size.width(), size.height());
    }

    /**
     * Draws a {@link Drawable} object onto this canvas at the specified position.
     * <p>
     * The drawable is rendered onto a temporary canvas and then merged into this canvas.
     *
     * @param drawable The {@link Drawable} to render.
     * @param x        The x-coordinate to draw at.
     * @param y        The y-coordinate to draw at.
     */
    public void draw(Drawable drawable, int x, int y) {
        final Canvas canvas = forDrawable(drawable);
        drawable.draw(canvas);
        merge(canvas, x, y);
    }

    /**
     * Draws a character at the specified position with no style.
     *
     * @param c the character to draw
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void draw(Character c, int x, int y) {
        draw(c, x, y, null);
    }

    /**
     * Draws a character at the specified position with an optional style.
     *
     * @param c     The character to draw.
     * @param x     The x-coordinate.
     * @param y     The y-coordinate.
     * @param paint The paint style to apply, or null for no style.
     */
    public void draw(Character c, int x, int y, Paint paint) {
        tiles[x][y] = c;
        paints[x][y] = paint;
    }

    /**
     * Merges another canvas into this canvas at the specified offset.
     * <p>
     * Only non-null characters and styles from the other canvas are copied.
     *
     * @param other   The canvas to merge.
     * @param xOffset The x offset to merge at.
     * @param yOffset The y offset to merge at.
     */
    void merge(Canvas other, int xOffset, int yOffset) {
        for (int x = 0; x < other.tiles.length; x++) {
            for (int y = 0; y < other.tiles[x].length; y++) {
                if (other.tiles[x][y] != null) {
                    tiles[x + xOffset][y + yOffset] = other.tiles[x][y];
                    paints[x + xOffset][y + yOffset] = other.paints[x][y];
                }
            }
        }
    }

    /**
     * Creates a new canvas sized to fit the given drawable.
     *
     * @param drawable The drawable to fit.
     * @return a new canvas sized for the drawable
     */
    static Canvas forDrawable(Drawable drawable) {
        return new Canvas(drawable.size.width(), drawable.size.height());
    }

    /**
     * Writes the contents of this canvas to the given {@link PrintWriter}.
     * <p>
     * Each row is written in order, with null cells rendered as spaces.
     *
     * @param writer The writer to output to.
     */
    public void writeTo(PrintWriter writer) {
        for (int y = 0; y < tiles[0].length; y++) {
            for (int x = 0; x < tiles.length; x++) {
                final Character tile = tiles[x][y];
                final Paint paint = paints[x][y];

                final Character c = tile == null ? ' ' : tile;

                if (paint != null) {
                    final AttributedStyle style = getStyle(paint);
                    final AttributedStringBuilder builder = new AttributedStringBuilder();
                    builder.style(style);
                    builder.append(c);
                    writer.print(builder.toAnsi());
                } else {
                    writer.print(c);
                }
            }
        }
    }

    static AttributedStyle getStyle(Paint paint) {
        AttributedStyle style = AttributedStyle.DEFAULT;

        if (paint.bold) {
            style = style.bold();
        } else {
            style = style.boldOff();
        }

        if (paint.italic) {
            style = style.italic();
        } else {
            style = style.italicOff();
        }

        if (paint.underline) {
            style = style.underline();
        } else {
            style = style.underlineOff();
        }

        if (paint.strikethrough) {
            style = style.crossedOut();
        } else {
            style = style.crossedOutOff();
        }

        if (paint.dim) {
            style = style.faint();
        } else {
            style = style.faintOff();
        }

        if (paint.inverted) {
            style = style.inverse();
        } else {
            style = style.inverseOff();
        }

        if (paint.blink) {
            style = style.blink();
        } else {
            style = style.blinkOff();
        }

        if (paint.foregroundColor == null) {
            style = style.foregroundOff();
        } else {
            style = style.foregroundRgb(paint.foregroundColor);
        }

        if (paint.backgroundColor == null) {
            style = style.backgroundOff();
        } else {
            style = style.backgroundRgb(paint.backgroundColor);
        }

        return style;
    }
}
