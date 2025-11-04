package org.pintoschneider.void_of_the_unfathomable.ui.core;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.pintoschneider.void_of_the_unfathomable.core.Offset;
import org.pintoschneider.void_of_the_unfathomable.game.ColorPalette;
import org.pintoschneider.void_of_the_unfathomable.ui.core.exceptions.OverflowException;
import org.pintoschneider.void_of_the_unfathomable.ui.core.exceptions.UIException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Canvas} class represents a 2D grid of characters and optional style objects,
 * used for rendering {@link Component} objects in a terminal or text-based UI.
 * <p>
 * Each cell in the canvas can hold a character and an associated style object.
 * The canvas provides methods to draw characters, draw other {@link Component} objects,
 * and merge other canvases into itself at specified offsets.
 * <p>
 * The canvas can be written to a {@link PrintWriter} for display.
 */
public final class Canvas {
    private final Character[][] tiles;
    private final Paint[][] paints;
    private final int width;
    private final int height;
    private final Component component;

    /**
     * Constructs a new {@link Canvas} with the specified width and height.
     *
     */
    public Canvas(Component component) {
        this.component = component;
        this.width = component.size.width();
        this.height = component.size.height();
        tiles = new Character[width][height];
        paints = new Paint[width][height];
    }

    static private AttributedStyle getStyle(Paint paint) {
        AttributedStyle style = AttributedStyle.DEFAULT;

        if (paint.bold()) {
            style = style.bold();
        } else {
            style = style.boldOff();
        }

        if (paint.italic()) {
            style = style.italic();
        } else {
            style = style.italicOff();
        }

        if (paint.underline()) {
            style = style.underline();
        } else {
            style = style.underlineOff();
        }

        if (paint.strikethrough()) {
            style = style.crossedOut();
        } else {
            style = style.crossedOutOff();
        }

        if (paint.dim()) {
            style = style.faint();
        } else {
            style = style.faintOff();
        }

        if (paint.inverted()) {
            style = style.inverse();
        } else {
            style = style.inverseOff();
        }

        if (paint.blink()) {
            style = style.blink();
        } else {
            style = style.blinkOff();
        }

        if (paint.foregroundColor() == null) {
            style = style.foregroundOff();
        } else {
            style = style.foregroundRgb(paint.foregroundColor().toInt());
        }

        if (paint.backgroundColor() == null) {
            style = style.backgroundOff();
        } else {
            style = style.backgroundRgb(paint.backgroundColor().toInt());
        }

        return style;
    }

    /**
     * Draws a {@link Component} object onto this canvas at the specified position.
     * <p>
     * The component is rendered onto a temporary canvas and then merged into this canvas.
     *
     * @param component The {@link Component} to render.
     * @param x         The x-coordinate to draw at.
     * @param y         The y-coordinate to draw at.
     */
    public void draw(Component component, int x, int y) {
        if (component.size() == null) {
            throw new IllegalStateException("Component size is null. Did you forget to call layout()?");
        }

        final Canvas canvas = new Canvas(component);

        try {
            component.draw(canvas);
        } catch (OverflowException e) {
            final Paint paint = new Paint().withBackgroundColor(ColorPalette.VERMILION);
            canvas.draw('‼', e.component().size().width() - 1, e.component().size().height() - 1, paint);
        }

        merge(canvas, x, y);
    }

    /**
     * Draws a {@link Component} object onto this canvas at the specified offset.
     *
     * @param component The {@link Component} to render.
     * @param offset    The {@link Offset} to draw at.
     */
    public void draw(Component component, Offset offset) {
        draw(component, offset.dx(), offset.dy());
    }

    /**
     * Draws a character at the specified position with no style.
     *
     * @param c the character to draw
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void draw(char c, int x, int y) {
        draw(c, x, y, null);
    }

    /**
     * Draws a character at the specified offset with no style.
     *
     * @param c      The character to draw.
     * @param offset The offset to draw at.
     */
    public void draw(char c, Offset offset) {
        draw(c, offset.dx(), offset.dy(), null);
    }

    /**
     * Draws a character at the specified position with an optional style.
     *
     * @param c     The character to draw.
     * @param x     The x-coordinate.
     * @param y     The y-coordinate.
     * @param paint The paint style to apply, or null for no style.
     */
    public void draw(char c, int x, int y, Paint paint) throws UIException {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new OverflowException(component, new Offset(x, y));
        }

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
        for (int x = 0; x < other.width; x++) {
            for (int y = 0; y < other.height; y++) {
                if (other.tiles[x][y] != null) {
                    if (x + xOffset < 0 || x + xOffset >= width || y + yOffset < 0 || y + yOffset >= height) {
                        continue; // Discard for now
                    }

                    tiles[x + xOffset][y + yOffset] = other.tiles[x][y];
                    paints[x + xOffset][y + yOffset] = other.paints[x][y];
                }
            }
        }
    }

    /**
     * Builds and returns a list of {@link AttributedString} objects representing the canvas content.
     * <p>
     * Each row is written in order, with null cells rendered as spaces.
     */
    public List<AttributedString> toAttributedStrings() {
        final List<AttributedString> lines = new ArrayList<>(this.height);

        for (int y = 0; y < height; y++) {
            final AttributedStringBuilder lineBuilder = new AttributedStringBuilder();

            for (int x = 0; x < width; x++) {
                final Character tile = tiles[x][y];
                final Paint paint = paints[x][y];

                final char ch = tile == null ? ' ' : tile;

                if (paint == null) {
                    lineBuilder.style(AttributedStyle.DEFAULT).append(ch);
                } else {
                    final AttributedStyle style = getStyle(paint);
                    lineBuilder.style(style).append(ch);
                }

            }

            lines.add(lineBuilder.toAttributedString());
        }

        return lines;
    }
}
